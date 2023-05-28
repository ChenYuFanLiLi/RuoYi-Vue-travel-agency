package com.ruoyi.travel.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import com.ruoyi.travel.domain.Customer;
import com.ruoyi.travel.service.ICustomerService;
import com.ruoyi.travel.vo.BookingVO;
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
import com.ruoyi.travel.domain.Booking;
import com.ruoyi.travel.service.IBookingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 收客记录Controller
 * 
 * @author 陈宇凡
 * @date 2023-05-05
 */
@RestController
@RequestMapping("/travel/booking")
@Api(value = "收客记录控制器", tags = {"收客记录管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingController extends BaseController
{
    private final IBookingService bookingService;

    private final ICustomerService customerService;


    /**
     * 查询收客记录列表
     */
    @ApiOperation("查询收客记录列表")
    @PreAuthorize("@ss.hasPermi('travel:booking:list')")
    @GetMapping("/list")
    public TableDataInfo list(Booking booking) {
        startPage();
        List<Booking> list = bookingService.list(new QueryWrapper<Booking>(booking));
        List<BookingVO> bookingVOList = new ArrayList<>();
        list.forEach(item->{
            BookingVO bookingVO = new BookingVO();
            BeanUtils.copyProperties(item,bookingVO);
            QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
            customerQueryWrapper.eq("booking_id",item.getId());
            bookingVO.setCustomerCount((long)customerService.count(customerQueryWrapper));
            bookingVOList.add(bookingVO);
        });
        TableDataInfo dataTable = getDataTable(list);
        dataTable.setRows(bookingVOList);
        return dataTable;
    }

    /**
     * 导出收客记录列表
     */
    @ApiOperation("导出收客记录列表")
    @PreAuthorize("@ss.hasPermi('travel:booking:export')")
    @Log(title = "收客记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Booking booking) {
        List<Booking> list = bookingService.list(new QueryWrapper<Booking>(booking));
        ExcelUtil<Booking> util = new ExcelUtil<Booking>(Booking.class);
        return util.exportExcel(list, "收客记录数据");
    }

    /**
     * 获取收客记录详细信息
     */
    @ApiOperation("获取收客记录详细信息")
    @PreAuthorize("@ss.hasPermi('travel:booking:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(bookingService.getById(id));
    }

    /**
     * 新增收客记录
     */
    @ApiOperation("新增收客记录")
    @PreAuthorize("@ss.hasPermi('travel:booking:add')")
    @Log(title = "收客记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Booking booking) {
        return toAjax(bookingService.save(booking));
    }

    /**
     * 修改收客记录
     */
    @ApiOperation("修改收客记录")
    @PreAuthorize("@ss.hasPermi('travel:booking:edit')")
    @Log(title = "收客记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Booking booking) {
        return toAjax(bookingService.updateById(booking));
    }

    /**
     * 删除收客记录
     */
    @ApiOperation("删除收客记录")
    @PreAuthorize("@ss.hasPermi('travel:booking:remove')")
    @Log(title = "收客记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bookingService.removeByIds(Arrays.asList(ids)));
    }
}
