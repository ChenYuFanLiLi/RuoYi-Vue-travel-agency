package com.ruoyi.travel.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mchange.lang.IntegerUtils;
import com.mchange.lang.LongUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.system.service.ISysUserService;
import com.ruoyi.travel.domain.*;
import com.ruoyi.travel.service.*;
import com.ruoyi.travel.vo.ItineraryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.DateFormatter;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

    private final IScheduleService scheduleService;

    private final IScheduleRoutingService scheduleRoutingService;

    private final IGroupService groupService;



    @GetMapping("/salesConfirmation")
    public void salesConfirmation(Long id){
        Itinerary itinerary = itineraryService.getById(id);

    }

    @PostMapping("/salesConfirmationBooking")
    public void salesConfirmationBooking(Long itineraryId,Long bookingId,HttpServletResponse response) throws IOException {
        Itinerary itinerary = itineraryService.getById(itineraryId);
        String itinerarySchedule = itinerary.getItinerarySchedule();
        Booking booking = bookingService.getById(bookingId);
        Group group = groupService.getById(booking.getGroupId());
        QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
        customerQueryWrapper.eq("booking_id",bookingId);
        List<Customer> customerList = customerService.list(customerQueryWrapper);
        Schedule schedule = scheduleService.getById(itinerarySchedule);
        Long scheduleId = schedule.getId();
        QueryWrapper<ScheduleRouting> scheduleRoutingQueryWrapper = new QueryWrapper<>();
        scheduleRoutingQueryWrapper.eq("schedule_id",scheduleId);
        List<ScheduleRouting> scheduleRoutingList = scheduleRoutingService.list(scheduleRoutingQueryWrapper);

        HashMap<String, Object> salesConfirmationExcelMap = new HashMap<>(20);

        salesConfirmationExcelMap.put("groupCompanyName",group.getGroupCompanyName());
        salesConfirmationExcelMap.put("groupRecipient",group.getGroupRecipient());
        salesConfirmationExcelMap.put("groupPhone",group.getGroupPhone());
        salesConfirmationExcelMap.put("groupMobile",group.getGroupMobile());
        salesConfirmationExcelMap.put("departureDate", new SimpleDateFormat("yyyy/MM/dd").format(itinerary.getDepartureDate()));
        salesConfirmationExcelMap.put("itineraryName",itinerary.getItineraryName());

        //奇数列
        ArrayList<HashMap<String, String>> customerOddExcelList = new ArrayList<>();
        //偶数列
        ArrayList<HashMap<String, String>> customerEvenExcelList = new ArrayList<>();

        for (int i = 0; i < customerList.size(); i++) {
            HashMap<String, String> customerMap = new HashMap<>();
            customerMap.put("serial",String.valueOf(i+1));
            customerMap.put("customerName",customerList.get(i).getCustomerName());
            customerMap.put("cardId",customerList.get(i).getCustomerIdNumber());
            customerMap.put("customerPhone",customerList.get(i).getCustomerContactInfo());
            if (i%2==0){
                customerOddExcelList.add(customerMap);
            }else {
                customerEvenExcelList.add(customerMap);
            }
        }

        ArrayList<HashMap<String,String>> scheduleRoutingExcelList = new ArrayList<>();

        for (ScheduleRouting scheduleRouting : scheduleRoutingList) {
            HashMap<String, String> scheduleRoutingMap = new HashMap<>();
            scheduleRoutingMap.put("date",new SimpleDateFormat("MM月dd日").format(DateUtils.addDays(itinerary.getDepartureDate(), scheduleRouting.getRoutingOrder().intValue())));
            scheduleRoutingMap.put("content",scheduleRouting.getRoutingContent());
            scheduleRoutingMap.put("place",scheduleRouting.getRoutingPlace());
            scheduleRoutingMap.put("meal",scheduleRouting.getRoutingMeal());
            scheduleRoutingExcelList.add(scheduleRoutingMap);
        }





//
//        salesConfirmationExcelMap.put("group",group);
//        salesConfirmationExcelMap.put("itinerary",itinerary);
//        salesConfirmationExcelMap.put("customerList",customerList);
//        salesConfirmationExcelMap.put("scheduleRoutingList",scheduleRoutingList);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(itinerary.getItineraryName()+group.getGroupName()+"销售项目确认表").replace("\\+","%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xls");
        Resource resource = new ClassPathResource("static/salesConfirmation.xlsx");



        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(resource.getInputStream()).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        excelWriter.fill(salesConfirmationExcelMap,writeSheet);
        excelWriter.fill(new FillWrapper("customerOdd",customerOddExcelList),fillConfig,writeSheet);
        excelWriter.fill(new FillWrapper("customerEven",customerEvenExcelList),fillConfig,writeSheet);
        excelWriter.fill(new FillWrapper("routing",scheduleRoutingExcelList),fillConfig,writeSheet);
        excelWriter.finish();
    }

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
