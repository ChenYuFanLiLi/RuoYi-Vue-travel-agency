package com.ruoyi.travel.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.travel.domain.Itinerary;
import com.ruoyi.travel.service.IItineraryService;
import com.ruoyi.travel.service.IOperationPlanService;
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
import com.ruoyi.travel.domain.CostAccounting;
import com.ruoyi.travel.service.ICostAccountingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

import javax.servlet.http.HttpServletResponse;

/**
 * 成本核算，用于记录团队成本核算信息Controller
 * 
 * @author 陈宇凡
 * @date 2023-05-21
 */
@RestController
@RequestMapping("/travel/accounting")
@Api(value = "成本核算，用于记录团队成本核算信息控制器", tags = {"成本核算，用于记录团队成本核算信息管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CostAccountingController extends BaseController
{
    private final ICostAccountingService costAccountingService;
    private final IItineraryService itineraryService;

    @ApiOperation("查询未选择行程表")
    @PreAuthorize("@ss.hasPermi('travel:itinerary:list')")
    @GetMapping("/listItinerary")
    public List<JSONObject> listItinerary() {
        QueryWrapper<Itinerary> queryWrapper = new QueryWrapper<>();
        queryWrapper.notExists("select 1 from travel_cost_accounting tca where tca.itinerary_id = travel_itinerary.id");
        List<Itinerary> list = itineraryService.list(queryWrapper);

        List<JSONObject> return_list = new ArrayList<>();

        for (Itinerary temp:list){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value",temp.getId()); //主键作为多选框的值
            jsonObject.put("label",temp.getItineraryName()); //名称作为多选框的标签
            return_list.add(jsonObject);
        }
        return return_list;
    }

    /**
     * 查询成本核算，用于记录团队成本核算信息列表
     */
    @ApiOperation("查询成本核算，用于记录团队成本核算信息列表")
    @PreAuthorize("@ss.hasPermi('travel:accounting:list')")
    @GetMapping("/list")
    public TableDataInfo list(CostAccounting costAccounting) {
        startPage();
        List<CostAccounting> list = costAccountingService.list(new QueryWrapper<CostAccounting>(costAccounting));
        return getDataTable(list);
    }

    /**
     * 导出成本核算，用于记录团队成本核算信息列表
     */
    @ApiOperation("导出成本核算，用于记录团队成本核算信息列表")
    @PreAuthorize("@ss.hasPermi('travel:accounting:export')")
    @Log(title = "成本核算，用于记录团队成本核算信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response,CostAccounting costAccounting) {
        List<CostAccounting> list = costAccountingService.list(new QueryWrapper<CostAccounting>(costAccounting));
        ExcelUtil<CostAccounting> util = new ExcelUtil<CostAccounting>(CostAccounting.class);
        util.exportExcel(response ,list, "成本核算，用于记录团队成本核算信息数据");
    }

    /**
     * 获取成本核算，用于记录团队成本核算信息详细信息
     */
    @ApiOperation("获取成本核算，用于记录团队成本核算信息详细信息")
    @PreAuthorize("@ss.hasPermi('travel:accounting:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(costAccountingService.getById(id));
    }

    /**
     * 新增成本核算，用于记录团队成本核算信息
     */
    @ApiOperation("新增成本核算，用于记录团队成本核算信息")
    @PreAuthorize("@ss.hasPermi('travel:accounting:add')")
    @Log(title = "成本核算，用于记录团队成本核算信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CostAccounting costAccounting) {
        return toAjax(costAccountingService.save(costAccounting));
    }

    /**
     * 修改成本核算，用于记录团队成本核算信息
     */
    @ApiOperation("修改成本核算，用于记录团队成本核算信息")
    @PreAuthorize("@ss.hasPermi('travel:accounting:edit')")
    @Log(title = "成本核算，用于记录团队成本核算信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CostAccounting costAccounting) {
        return toAjax(costAccountingService.updateById(costAccounting));
    }

    /**
     * 删除成本核算，用于记录团队成本核算信息
     */
    @ApiOperation("删除成本核算，用于记录团队成本核算信息")
    @PreAuthorize("@ss.hasPermi('travel:accounting:remove')")
    @Log(title = "成本核算，用于记录团队成本核算信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(costAccountingService.removeByIds(Arrays.asList(ids)));
    }
}
