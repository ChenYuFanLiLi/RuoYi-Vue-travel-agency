package com.ruoyi.travel.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.travel.domain.Booking;
import com.ruoyi.travel.domain.Group;
import com.ruoyi.travel.domain.Itinerary;
import com.ruoyi.travel.dto.CustomerDTO;
import com.ruoyi.travel.service.IBookingService;
import com.ruoyi.travel.service.IGroupService;
import com.ruoyi.travel.service.IItineraryService;
import com.ruoyi.travel.template.CustomerTemplate;
import com.ruoyi.travel.vo.CustomerVO;
import io.netty.util.internal.StringUtil;
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
import com.ruoyi.travel.domain.Customer;
import com.ruoyi.travel.service.ICustomerService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

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

    private final IBookingService bookingService;

    private final IGroupService groupService;

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
    public TableDataInfo listByItineraryId(CustomerDTO customerDTO){
        QueryWrapper<Booking> bookingQueryWrapper = new QueryWrapper<>();
        bookingQueryWrapper.eq("itinerary_id",customerDTO.getItineraryId());
        List<Long> bookingList = bookingService.list(bookingQueryWrapper).stream().map(Booking::getId).collect(Collectors.toList());
        QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
        customerQueryWrapper.and(wrapper->wrapper.eq("itinerary_id",customerDTO.getItineraryId()).or().in("booking_id",bookingList));
        customerQueryWrapper.like(StringUtils.isNotEmpty(customerDTO.getCustomerName()),"customer_name",customerDTO.getCustomerName());
        customerQueryWrapper.like(StringUtils.isNotEmpty(customerDTO.getCustomerIdNumber()),"customer_id_number",customerDTO.getCustomerIdNumber());
        customerQueryWrapper.like(StringUtils.isNotEmpty(customerDTO.getCustomerContactInfo()),"customer_contact_info",customerDTO.getCustomerContactInfo());
        startPage();
        List<Customer> list = customerService.list(customerQueryWrapper);
        TableDataInfo dataTable = getDataTable(list);
        List<CustomerVO> customerVOList = list.stream().map(customer -> {
            CustomerVO customerVO = new CustomerVO();
            BeanUtils.copyProperties(customer, customerVO);
            if (customer.getGroupId() != null) {
                Group group = groupService.getById(customer.getGroupId());
                customerVO.setGroupName(group.getGroupName());
            } else {
                customerVO.setGroupName("散客");
            }
            return customerVO;
        }).collect(Collectors.toList());
        dataTable.setRows(customerVOList);
        return dataTable;
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

    /**
     * 导入模板
     * @param response 返回模板文件
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response){
        ExcelUtil<CustomerTemplate> util = new ExcelUtil<>(CustomerTemplate.class);
        util.importTemplateExcel(response,"客户信息");
    }

    @Log(title = "客户导入",businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('travel:customer:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, Long itineraryId,Long bookingId,Long groupId) throws Exception{
        ExcelUtil<Customer> util = new ExcelUtil<>(Customer.class);
        List<Customer> customerList = util.importExcel(file.getInputStream());
        if (customerList.size()>0&&(itineraryId!=null||(bookingId!=null&&groupId!=null))){
            List<Customer> customers = customerList.stream().map(item -> (item.setItineraryId(itineraryId).setBookingId(bookingId).setGroupId(groupId))).collect(Collectors.toList());
            String message = customerService.importCustomer(customers);
            return success(message);
        }else {
            return AjaxResult.error("导入失败，请检查是否依据模板导入，或数据格式不正确");
        }
    }
}
