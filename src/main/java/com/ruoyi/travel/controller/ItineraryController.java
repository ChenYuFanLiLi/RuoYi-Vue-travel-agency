package com.ruoyi.travel.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.ruoyi.project.system.service.ISysUserService;
import com.ruoyi.travel.domain.Booking;
import com.ruoyi.travel.domain.Customer;
import com.ruoyi.travel.service.IBookingService;
import com.ruoyi.travel.service.ICustomerService;
import com.ruoyi.travel.vo.ItineraryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.travel.domain.Itinerary;
import com.ruoyi.travel.service.IItineraryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

import javax.servlet.http.HttpServletResponse;

/**
 * 行程Controller
 * 
 * @author 陈宇凡
 * @date 2023-05-05
 */
@RestController
@RequestMapping("/travel/itinerary")
@Api(value = "行程控制器", tags = {"行程管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItineraryController extends BaseController
{
    private final IItineraryService itineraryService;

    private final IBookingService bookingService;

    private final ICustomerService customerService;

    private final ISysUserService sysUserService;

    /**
     * 查询行程列表
     */
    @ApiOperation("查询行程列表")
    @PreAuthorize("@ss.hasPermi('travel:itinerary:list')")
    @GetMapping("/list")
    public TableDataInfo list(Itinerary itinerary) {
        startPage();
        List<Itinerary> list = itineraryService.list(new QueryWrapper<Itinerary>(itinerary));
        TableDataInfo dataTable = getDataTable(list);
        ArrayList<ItineraryVO> itineraryVOList = new ArrayList<>();
        list.forEach(item->{
            ItineraryVO itineraryVO = new ItineraryVO();
            BeanUtils.copyProperties(item,itineraryVO);
            QueryWrapper<Booking> bookingQueryWrapper = new QueryWrapper<>();
            bookingQueryWrapper.eq("itinerary_id",item.getId());
            List<Booking> bookingList = bookingService.list(bookingQueryWrapper);
            if (bookingList.size()>0){
                itineraryVO.setItineraryObligate(bookingList.stream().mapToLong(Booking::getBookingCount).summaryStatistics().getSum());
                QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
                customerQueryWrapper.eq("itinerary_id",item.getId()).or().in("booking_id",bookingList.stream().map(Booking::getId).collect(Collectors.toList()));
                itineraryVO.setItineraryConfirm((long) customerService.count(customerQueryWrapper));
                StringBuilder builder = new StringBuilder();
                bookingList.forEach(booking->{
                    builder.append(booking.getGroupName());
                    builder.append("-");
                    builder.append(booking.getGroupLeaderName());
                    builder.append(":");
                    builder.append(booking.getBookingCount());
                    builder.append("人");
                    if (booking.getBookerId()!=null){
                        String nickName ="("+ sysUserService.selectUserById(booking.getBookerId()).getNickName()+")";
                        builder.append(nickName);
                    }else {
                        builder.append("未知");
                    }
                    builder.append("， ");
                });
                itineraryVO.setClientBrief(builder.toString());

                //客户数
                Long customerNum = 0L;

                for (Booking booking : bookingList) {
                    Long bookingCustomerNum = 0L;
                    QueryWrapper<Customer> customerQueryWrapper1 = new QueryWrapper<>();
                    customerQueryWrapper1.eq("booking_id", booking.getId());
                    int count = customerService.count(customerQueryWrapper1);
                    if (booking.getBookingCount() <= count) {
                        bookingCustomerNum = (long) count;
                    } else {
                        bookingCustomerNum = booking.getBookingCount();
                    }
                    customerNum+=bookingCustomerNum;
                }

                QueryWrapper<Customer> customerQueryWrapper1 = new QueryWrapper<>();
                customerQueryWrapper1.eq("itinerary_id",item.getId());
                int count = customerService.count(customerQueryWrapper1);
                customerNum +=(long) count;

                if (customerNum>=item.getPlanQuantity()){
                    itineraryVO.setItineraryRemaining(0L);
                    itineraryVO.setItineraryOverCollection(customerNum-item.getPlanQuantity());
                }else {
                    itineraryVO.setItineraryOverCollection(0L);
                    itineraryVO.setItineraryRemaining(item.getPlanQuantity()-customerNum);
                }
            }
            itineraryVOList.add(itineraryVO);
        });
        dataTable.setRows(itineraryVOList);
        return dataTable;
    }

    /**
     * 导出行程列表
     */
    @ApiOperation("导出行程列表")
    @PreAuthorize("@ss.hasPermi('travel:itinerary:export')")
    @Log(title = "行程", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Itinerary itinerary) {
        List<Itinerary> list = itineraryService.list(new QueryWrapper<Itinerary>(itinerary));
        ExcelUtil<Itinerary> util = new ExcelUtil<Itinerary>(Itinerary.class);
        util.exportExcel(response,list, "行程数据");
    }

    /**
     * 获取行程详细信息
     */
    @ApiOperation("获取行程详细信息")
    @PreAuthorize("@ss.hasPermi('travel:itinerary:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(itineraryService.getById(id));
    }

    /**
     * 新增行程
     */
    @ApiOperation("新增行程")
    @PreAuthorize("@ss.hasPermi('travel:itinerary:add')")
    @Log(title = "行程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Itinerary itinerary) {
        return toAjax(itineraryService.save(itinerary));
    }

    /**
     * 修改行程
     */
    @ApiOperation("修改行程")
    @PreAuthorize("@ss.hasPermi('travel:itinerary:edit')")
    @Log(title = "行程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Itinerary itinerary) {
        return toAjax(itineraryService.updateById(itinerary));
    }

    /**
     * 删除行程
     */
    @ApiOperation("删除行程")
    @PreAuthorize("@ss.hasPermi('travel:itinerary:remove')")
    @Log(title = "行程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(itineraryService.removeByIds(Arrays.asList(ids)));
    }
}
