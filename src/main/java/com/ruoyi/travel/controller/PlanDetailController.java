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
import com.ruoyi.travel.domain.PlanDetail;
import com.ruoyi.travel.service.IPlanDetailService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 计划项目明细Controller
 * 
 * @author 陈宇凡
 * @date 2023-05-07
 */
@RestController
@RequestMapping("/travel/planDetail")
@Api(value = "计划项目明细控制器", tags = {"计划项目明细管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PlanDetailController extends BaseController
{
    private final IPlanDetailService planDetailService;

    /**
     * 查询计划项目明细列表
     */
    @ApiOperation("查询计划项目明细列表")
    @PreAuthorize("@ss.hasPermi('travel:planDetail:list')")
    @GetMapping("/list")
    public TableDataInfo list(PlanDetail planDetail) {
        startPage();
        List<PlanDetail> list = planDetailService.list(new QueryWrapper<PlanDetail>(planDetail));
        return getDataTable(list);
    }

    /**
     * 导出计划项目明细列表
     */
    @ApiOperation("导出计划项目明细列表")
    @PreAuthorize("@ss.hasPermi('travel:planDetail:export')")
    @Log(title = "计划项目明细", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PlanDetail planDetail) {
        List<PlanDetail> list = planDetailService.list(new QueryWrapper<PlanDetail>(planDetail));
        ExcelUtil<PlanDetail> util = new ExcelUtil<PlanDetail>(PlanDetail.class);
        return util.exportExcel(list, "计划项目明细数据");
    }

    /**
     * 获取计划项目明细详细信息
     */
    @ApiOperation("获取计划项目明细详细信息")
    @PreAuthorize("@ss.hasPermi('travel:planDetail:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(planDetailService.getById(id));
    }

    /**
     * 新增计划项目明细
     */
    @ApiOperation("新增计划项目明细")
    @PreAuthorize("@ss.hasPermi('travel:planDetail:add')")
    @Log(title = "计划项目明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PlanDetail planDetail) {
        return toAjax(planDetailService.save(planDetail));
    }

    /**
     * 修改计划项目明细
     */
    @ApiOperation("修改计划项目明细")
    @PreAuthorize("@ss.hasPermi('travel:planDetail:edit')")
    @Log(title = "计划项目明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PlanDetail planDetail) {
        return toAjax(planDetailService.updateById(planDetail));
    }

    /**
     * 删除计划项目明细
     */
    @ApiOperation("删除计划项目明细")
    @PreAuthorize("@ss.hasPermi('travel:planDetail:remove')")
    @Log(title = "计划项目明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(planDetailService.removeByIds(Arrays.asList(ids)));
    }
}
