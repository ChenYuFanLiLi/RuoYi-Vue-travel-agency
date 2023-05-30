package com.ruoyi.travel.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.travel.domain.*;
import com.ruoyi.travel.service.*;
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
    private final IOperationPlanService operationPlanService;
    private final IPlanDetailService planDetailService;
    private final ICostDetailService costDetailService;
    private final ICashDetailService cashDetailService;
    /**
     * 查询行程，团号，出发日期，团队编号汇总
     * @return
     */

    @ApiOperation("查询未选择行程表")
    @PreAuthorize("@ss.hasPermi('travel:itinerary:list')")
    @GetMapping("/listItinerary")
    public List<JSONObject> listItinerary() {
        List<OperationPlan> list = operationPlanService.list(new QueryWrapper<OperationPlan>()
                .notInSql("id", "select itinerary_id from travel_cost_accounting"));

        List<JSONObject> return_list = new ArrayList<>();

        for (OperationPlan temp:list){
            Itinerary itinerary = itineraryService.getById(temp.getItineraryId());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value",itinerary.getId()); //主键作为多选框的值
            jsonObject.put("label",itinerary.getItineraryName()); //名称作为多选框的标签
            jsonObject.put("team_id",temp.getTeamId()); //团队编号
            jsonObject.put("group_number",temp.getGroupNumber()); //团号
            jsonObject.put("plan_departure_date",temp.getPlanDepartureDate()); //出发日期
            jsonObject.put("operation_plan_id",temp.getId());
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
        costAccountingService.save(costAccounting);
//        成本核算创建时，自动导入操作计划明细
        List<PlanDetail> planDetailList = planDetailService.list(new QueryWrapper<PlanDetail>()
                .eq("operation_plan_id",costAccounting.getOperationPlanId()));
        Long id = costAccountingService.getOne(
                        new QueryWrapper<CostAccounting>()
                                .eq("itinerary_id",costAccounting.getItineraryId()))
                        .getId();
        for (PlanDetail planDetail:planDetailList){
            CostDetail costDetail = new CostDetail();
            costDetail.setOperationCostId(id);
            costDetail.setTravelScheduleId(planDetail.getTravelScheduleId());
            costDetail.setProjectId(String.valueOf(planDetail.getProjectId()));
            costDetail.setPlanType(planDetail.getPlanType());
            costDetail.setProjectName(planDetail.getProjectName());
            costDetail.setProjectCost(planDetail.getProjectCost());
            costDetail.setProjectAmount(planDetail.getProjectQuantity());
            costDetail.setProjectUnit(planDetail.getProjectUnit());
            costDetail.setCostAmount(planDetail.getPlanAmount());
            costDetail.setCostCash(planDetail.getPlanCash());
            costDetail.setCostCard(planDetail.getPlanCard());
            costDetail.setCostTransfer(planDetail.getPlanTransfer());
            costDetail.setCostOffset(planDetail.getPlanOffset());
            costDetail.setCostOnCredit(planDetail.getPlanCredit());
            costDetail.setCostOnCredit(planDetail.getPlanCredit());
            costDetailService.save(costDetail);
        }
        costAccountingService.calcTrends(id);
        return AjaxResult.success("1");
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
        AjaxResult ajaxResult =toAjax(costAccountingService.removeByIds(Arrays.asList(ids)));
        for (Long id:ids){
            QueryWrapper<CostDetail> queryWrapper = new QueryWrapper();
            queryWrapper.eq("operation_cost_id",id);
            costDetailService.remove(queryWrapper);

            QueryWrapper<CashDetail> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("related_id",id).eq("cash_type","cost");
            cashDetailService.remove(queryWrapper1);
        }
        return ajaxResult;
    }
}
