package com.ruoyi.travel.controller;

import java.util.List;
import java.util.Arrays;

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
import com.ruoyi.travel.domain.CashDetail;
import com.ruoyi.travel.service.ICashDetailService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

import javax.servlet.http.HttpServletResponse;

/**
 * 现金明细Controller
 * 
 * @author 陈宇凡
 * @date 2023-05-30
 */
@RestController
@RequestMapping("/travel/cashdetail")
@Api(value = "现金明细控制器", tags = {"现金明细管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CashDetailController extends BaseController
{
    private final ICashDetailService cashDetailService;

    /**
     * 查询现金明细列表
     */
    @ApiOperation("查询现金明细列表")
    @PreAuthorize("@ss.hasPermi('travel:accounting:list')")
    @GetMapping("/list")
    public TableDataInfo list(CashDetail cashDetail) {
        startPage();
        List<CashDetail> list = cashDetailService.list(new QueryWrapper<CashDetail>(cashDetail));
        return getDataTable(list);
    }

    /**
     * 导出现金明细列表
     */
    @ApiOperation("导出现金明细列表")
    @PreAuthorize("@ss.hasPermi('travel:accounting:export')")
    @Log(title = "现金明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response,CashDetail cashDetail) {
        List<CashDetail> list = cashDetailService.list(new QueryWrapper<CashDetail>(cashDetail));
        ExcelUtil<CashDetail> util = new ExcelUtil<CashDetail>(CashDetail.class);
        util.exportExcel(response ,list, "现金明细数据");
    }

    /**
     * 获取现金明细详细信息
     */
    @ApiOperation("获取现金明细详细信息")
    @PreAuthorize("@ss.hasPermi('travel:accounting:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(cashDetailService.getById(id));
    }

    /**
     * 新增现金明细
     */
    @ApiOperation("新增现金明细")
    @PreAuthorize("@ss.hasPermi('travel:accounting:add')")
    @Log(title = "现金明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CashDetail cashDetail) {
        return toAjax(cashDetailService.save(cashDetail));
    }

    /**
     * 修改现金明细
     */
    @ApiOperation("修改现金明细")
    @PreAuthorize("@ss.hasPermi('travel:accounting:edit')")
    @Log(title = "现金明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CashDetail cashDetail) {
        return toAjax(cashDetailService.updateById(cashDetail));
    }

    /**
     * 删除现金明细
     */
    @ApiOperation("删除现金明细")
    @PreAuthorize("@ss.hasPermi('travel:accounting:remove')")
    @Log(title = "现金明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(cashDetailService.removeByIds(Arrays.asList(ids)));
    }
}
