package com.ruoyi.travel.controller;

import java.util.List;
import java.util.Arrays;

import com.ruoyi.travel.service.IOperationPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.loadtime.Aj;
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

import javax.servlet.http.HttpServletResponse;

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
    private final IOperationPlanService operationPlanService;

    /**
     * 查询计划项目明细列表
     */
    @ApiOperation("查询计划项目明细列表")
    @PreAuthorize("@ss.hasPermi('travel:plan:list')")
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
    @PreAuthorize("@ss.hasPermi('travel:plan:export')")
    @Log(title = "计划项目明细", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public void export(HttpServletResponse response, PlanDetail planDetail) {
        List<PlanDetail> list = planDetailService.list(new QueryWrapper<PlanDetail>(planDetail));
        ExcelUtil<PlanDetail> util = new ExcelUtil<PlanDetail>(PlanDetail.class);
        util.exportExcel(response ,list, "计划项目明细数据");
    }

    /**
     * 获取计划项目明细详细信息
     */
    @ApiOperation("获取计划项目明细详细信息")
    @PreAuthorize("@ss.hasPermi('travel:plan:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(planDetailService.getById(id));
    }

    /**
     * 新增计划项目明细
     */
    @ApiOperation("新增计划项目明细")
    @PreAuthorize("@ss.hasPermi('travel:plan:add')")
    @Log(title = "计划项目明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PlanDetail planDetail) {
        AjaxResult ajaxResult = toAjax(planDetailService.save(planDetail));
        operationPlanService.calcTrends(planDetail.getOperationPlanId());
        return ajaxResult;
    }

    /**
     * 修改计划项目明细
     */
    @ApiOperation("修改计划项目明细")
    @PreAuthorize("@ss.hasPermi('travel:plan:edit')")
    @Log(title = "计划项目明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PlanDetail planDetail) {
        AjaxResult ajaxResult = toAjax(planDetailService.updateById(planDetail));
        operationPlanService.calcTrends(planDetail.getOperationPlanId());
        return ajaxResult;
    }

    /**
     * 删除计划项目明细
     */
    @ApiOperation("删除计划项目明细")
    @PreAuthorize("@ss.hasPermi('travel:plan:remove')")
    @Log(title = "计划项目明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        PlanDetail planDetail = planDetailService.getById(ids[0]);
        AjaxResult ajaxResult = toAjax(planDetailService.removeByIds(Arrays.asList(ids)));
        operationPlanService.calcTrends(planDetail.getOperationPlanId());
        return ajaxResult;
    }
}
