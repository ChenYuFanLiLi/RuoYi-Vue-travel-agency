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
import com.ruoyi.travel.domain.CostDetail;
import com.ruoyi.travel.service.ICostDetailService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

import javax.servlet.http.HttpServletResponse;

/**
 * 成本核算明细Controller
 * 
 * @author 陈宇凡
 * @date 2023-05-21
 */
@RestController
@RequestMapping("/travel/costdetail")
@Api(value = "成本核算明细控制器", tags = {"成本核算明细管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CostDetailController extends BaseController
{
    private final ICostDetailService costDetailService;

    /**
     * 查询成本核算明细列表
     */
    @ApiOperation("查询成本核算明细列表")
    @PreAuthorize("@ss.hasPermi('travel:costdetail:list')")
    @GetMapping("/list")
    public TableDataInfo list(CostDetail costDetail) {
        startPage();
        List<CostDetail> list = costDetailService.list(new QueryWrapper<CostDetail>(costDetail));
        return getDataTable(list);
    }

    /**
     * 导出成本核算明细列表
     */
    @ApiOperation("导出成本核算明细列表")
    @PreAuthorize("@ss.hasPermi('travel:costdetail:export')")
    @Log(title = "成本核算明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response,CostDetail costDetail) {
        List<CostDetail> list = costDetailService.list(new QueryWrapper<CostDetail>(costDetail));
        ExcelUtil<CostDetail> util = new ExcelUtil<CostDetail>(CostDetail.class);
        util.exportExcel(response ,list, "成本核算明细数据");
    }

    /**
     * 获取成本核算明细详细信息
     */
    @ApiOperation("获取成本核算明细详细信息")
    @PreAuthorize("@ss.hasPermi('travel:costdetail:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(costDetailService.getById(id));
    }

    /**
     * 新增成本核算明细
     */
    @ApiOperation("新增成本核算明细")
    @PreAuthorize("@ss.hasPermi('travel:costdetail:add')")
    @Log(title = "成本核算明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CostDetail costDetail) {
        return toAjax(costDetailService.save(costDetail));
    }

    /**
     * 修改成本核算明细
     */
    @ApiOperation("修改成本核算明细")
    @PreAuthorize("@ss.hasPermi('travel:costdetail:edit')")
    @Log(title = "成本核算明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CostDetail costDetail) {
        return toAjax(costDetailService.updateById(costDetail));
    }

    /**
     * 删除成本核算明细
     */
    @ApiOperation("删除成本核算明细")
    @PreAuthorize("@ss.hasPermi('travel:costdetail:remove')")
    @Log(title = "成本核算明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(costDetailService.removeByIds(Arrays.asList(ids)));
    }
}
