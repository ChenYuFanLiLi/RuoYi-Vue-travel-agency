package com.ruoyi.travel.controller;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.ruoyi.travel.domain.Booking;
import com.ruoyi.travel.service.IBookingService;
import com.ruoyi.travel.service.IItineraryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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
import com.ruoyi.travel.domain.Customer;
import com.ruoyi.travel.service.ICustomerService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 客户信息Controller
 * 
 * @author 陈宇凡
 * @date 2023-05-05
 */
@RestController
@RequestMapping("/travel/customer")
@Api(value = "客户信息控制器", tags = {"客户信息管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CustomerController extends BaseController
{
    private final ICustomerService customerService;

//    private final IItineraryService itineraryService;

    private final IBookingService bookingService;

    /**
     * 查询客户信息列表
     */
    @ApiOperation("查询客户信息列表")
    @PreAuthorize("@ss.hasPermi('travel:customer:list')")
    @GetMapping("/list")
    public TableDataInfo list(Customer customer) {
        startPage();
        List<Customer> list = customerService.list(new QueryWrapper<Customer>(customer));
        return getDataTable(list);
    }

    @ApiOperation("通过行程Id查询客户信息列表")
    @PreAuthorize("@ss.hasPermi('travel:customer:list')")
    @GetMapping("/listByItineraryId")
    public TableDataInfo listByItineraryId(Long itineraryId){
        QueryWrapper<Booking> bookingQueryWrapper = new QueryWrapper<>();
        bookingQueryWrapper.eq("itinerary_id",itineraryId);
        List<Long> bookingList = bookingService.list(bookingQueryWrapper).stream().map(Booking::getId).collect(Collectors.toList());
        QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
        customerQueryWrapper.in("booking_id",bookingList);
        startPage();
        List<Customer> list = customerService.list(customerQueryWrapper);
        return getDataTable(list);
    }

    /**
     * 导出客户信息列表
     */
    @ApiOperation("导出客户信息列表")
    @PreAuthorize("@ss.hasPermi('travel:customer:export')")
    @Log(title = "客户信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Customer customer) {
        List<Customer> list = customerService.list(new QueryWrapper<Customer>(customer));
        ExcelUtil<Customer> util = new ExcelUtil<Customer>(Customer.class);
        return util.exportExcel(list, "客户信息数据");
    }

    /**
     * 获取客户信息详细信息
     */
    @ApiOperation("获取客户信息详细信息")
    @PreAuthorize("@ss.hasPermi('travel:customer:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(customerService.getById(id));
    }

    /**
     * 新增客户信息
     */
    @ApiOperation("新增客户信息")
    @PreAuthorize("@ss.hasPermi('travel:customer:add')")
    @Log(title = "客户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Customer customer) {
        return toAjax(customerService.save(customer));
    }

    /**
     * 修改客户信息
     */
    @ApiOperation("修改客户信息")
    @PreAuthorize("@ss.hasPermi('travel:customer:edit')")
    @Log(title = "客户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Customer customer) {
        return toAjax(customerService.updateById(customer));
    }

    /**
     * 删除客户信息
     */
    @ApiOperation("删除客户信息")
    @PreAuthorize("@ss.hasPermi('travel:customer:remove')")
    @Log(title = "客户信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(customerService.removeByIds(Arrays.asList(ids)));
    }
}
