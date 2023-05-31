package com.ruoyi.travel.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.travel.domain.*;
import com.ruoyi.travel.service.ICashDetailService;
import com.ruoyi.travel.service.IItineraryService;
import com.ruoyi.travel.service.IPlanDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
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
import com.ruoyi.travel.service.IOperationPlanService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

import javax.servlet.http.HttpServletResponse;

/**
 * 操作计划Controller
 * 
 * @author 陈宇凡
 * @date 2023-05-07
 */
@RestController
@RequestMapping("/travel/plan")
@Api(value = "操作计划控制器", tags = {"操作计划管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OperationPlanController extends BaseController
{
    private final IOperationPlanService operationPlanService;
    private final IItineraryService itineraryService;
    private final RedisTemplate redisTemplate;
    private final IPlanDetailService planDetailService;
    private final ICashDetailService cashDetailService;
    /**
     * 查询未选择行程表
     */
    @ApiOperation("查询未选择行程表")
    @PreAuthorize("@ss.hasPermi('travel:itinerary:list')")
    @GetMapping("/listItinerary")
    public List<JSONObject> listItinerary() {
        QueryWrapper<Itinerary> queryWrapper = new QueryWrapper<>();
        queryWrapper.notExists("select 1 from travel_operation_plan trop where trop.itinerary_id = travel_itinerary.id");
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
     * 查询操作计划列表
     */
    @ApiOperation("查询操作计划列表")
    @PreAuthorize("@ss.hasPermi('travel:plan:list')")
    @GetMapping("/list")
    public TableDataInfo list(OperationPlan operationPlan) {
        startPage();
        List<OperationPlan> list = operationPlanService.list(new QueryWrapper<OperationPlan>(operationPlan));
        return getDataTable(list);
    }

    /**
     * 导出操作计划列表
     */
//    @ApiOperation("导出操作计划列表")
//    @PreAuthorize("@ss.hasPermi('travel:plan:export')")
//    @Log(title = "操作计划", businessType = BusinessType.EXPORT)
//    @RequestMapping("/export")
//    public void export(HttpServletResponse response,OperationPlan operationPlan) {
//        List<OperationPlan> list = operationPlanService.list(new QueryWrapper<OperationPlan>(operationPlan));
//        ExcelUtil<OperationPlan> util = new ExcelUtil<OperationPlan>(OperationPlan.class);
//        util.exportExcel(response ,list, "plan");
//    }
    @ApiOperation("导出操作计划列表")
    @PreAuthorize("@ss.hasPermi('travel:plan:export')")
    @Log(title = "操作计划", businessType = BusinessType.EXPORT)
    @RequestMapping("/export")
    public void export(Long planId,HttpServletResponse response) throws IOException {
        if(planId == null)
            return;

        Resource resource = new ClassPathResource("static/operationPlan.xlsx");
        String templateFileName = resource.getFile().getPath();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + "test" + ".xls");

        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateFileName).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();

        Map<String,Object> map = MapUtils.newHashMap();

        DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");

        //导游姓名，导游电话
        OperationPlan operationPlan = operationPlanService.getById(planId);
        map.put("guideName",operationPlan.getGuideName());
        map.put("guidePhone",operationPlan.getGuidePhone());
        map.put("groupNumber",operationPlan.getGroupNumber());
        map.put("date",dateFormat.format(operationPlan.getPlanDepartureDate()));
        map.put("teamId",operationPlan.getTeamId());

        //明细合计项
        BigDecimal amountTotal = BigDecimal.valueOf(0);
        BigDecimal cashTotal = BigDecimal.valueOf(0);
        BigDecimal cardTotal = BigDecimal.valueOf(0);
        BigDecimal transferTotal = BigDecimal.valueOf(0);
        BigDecimal offsetTotal = BigDecimal.valueOf(0);
        BigDecimal creditTotal = BigDecimal.valueOf(0);

        //现收合计项
        BigDecimal cashAmountTotal = BigDecimal.valueOf(0);
        BigDecimal cashDiscountTotal = BigDecimal.valueOf(0);

        //现收差值
        BigDecimal AmountSub = BigDecimal.valueOf(0);

        //计划明细
        List<Map<String,Object>> planDetailList = new ArrayList<>();
        QueryWrapper<PlanDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operation_plan_id",planId);
        List<PlanDetail> list = planDetailService.list(queryWrapper);
        Long planDetailNo = 0L;
        for(PlanDetail planDetail:list){
            Map<String,Object> tempMap = MapUtils.newHashMap();

            tempMap.put("No",++planDetailNo);
            tempMap.put("date",dateFormat.format(operationPlan.getPlanDepartureDate()));
            tempMap.put("type",planDetail.getPlanType());
            tempMap.put("item",planDetail.getProjectName());
            tempMap.put("cost",planDetail.getProjectCost());
            tempMap.put("unit",planDetail.getProjectUnit());
            tempMap.put("quantity",planDetail.getProjectQuantity());

            tempMap.put("amount",planDetail.getPlanAmount());
            amountTotal = amountTotal.add(planDetail.getPlanAmount());
            tempMap.put("cash",planDetail.getPlanCash());
            cashTotal = cashTotal.add(planDetail.getPlanCash());
            tempMap.put("card",planDetail.getPlanCard());
            cardTotal = cardTotal.add(planDetail.getPlanCard());
            tempMap.put("transfer",planDetail.getPlanTransfer());
            transferTotal = transferTotal.add(planDetail.getPlanTransfer());
            tempMap.put("offset",planDetail.getPlanOffset());
            offsetTotal = offsetTotal.add(planDetail.getPlanOffset());
            tempMap.put("credit",planDetail.getPlanCredit());
            creditTotal = creditTotal.add(planDetail.getPlanCredit());
            tempMap.put("remark",planDetail.getRemark());

            planDetailList.add(tempMap);
        }

        //现收明细
        List<Map<String,Object>> cashList = new ArrayList<>();
        QueryWrapper<CashDetail> queryWrapperCash = new QueryWrapper<>();
        queryWrapperCash.eq("related_id",planId);
        queryWrapperCash.eq("cash_type","plan");
        List<CashDetail> listCash = cashDetailService.list(queryWrapperCash);
        for(CashDetail cashDetail:listCash){
            Map<String,Object> tempMap = MapUtils.newHashMap();

            tempMap.put("project",cashDetail.getCashProject());
            tempMap.put("price",cashDetail.getCashUnitPrice());
            tempMap.put("unit",cashDetail.getCashUnit());
            tempMap.put("quantity",cashDetail.getCashQuantity());
            tempMap.put("remark",cashDetail.getRemark());

            tempMap.put("amount",cashDetail.getCashAmount());
            cashAmountTotal = cashAmountTotal.add(cashDetail.getCashAmount());
            tempMap.put("discount",cashDetail.getCashDiscount());
            cashDiscountTotal = cashDiscountTotal.add(cashDetail.getCashDiscount());

            cashList.add(tempMap);
        }

        //合计计算
        AmountSub = cashTotal.subtract(cashAmountTotal);

        //合计填充
        map.put("amountTotal",amountTotal);
        map.put("cashTotal",cashTotal);
        map.put("cardTotal",cardTotal);
        map.put("transferTotal",transferTotal);
        map.put("offsetTotal",offsetTotal);
        map.put("creditTotal",creditTotal);

        map.put("cashAmountTotal",cashAmountTotal);
        map.put("cashDiscountTotal",cashDiscountTotal);

        map.put("AmountSub",AmountSub);

        excelWriter.fill(map,writeSheet);
        excelWriter.fill(new FillWrapper("planDetail",planDetailList),fillConfig,writeSheet);
        excelWriter.fill(new FillWrapper("cash",cashList),fillConfig,writeSheet);
        excelWriter.finish();

    }

    /**
     * 获取操作计划详细信息
     */
    @ApiOperation("获取操作计划详细信息")
    @PreAuthorize("@ss.hasPermi('travel:plan:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(operationPlanService.getById(id));
    }

    /**
     * 新增操作计划
     */
    @ApiOperation("新增操作计划")
    @PreAuthorize("@ss.hasPermi('travel:plan:add')")
    @Log(title = "操作计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OperationPlan operationPlan) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(operationPlan.getPlanDepartureDate());
        String prefix = "JSJ-"+dateString+"-";

        String key = "plan_add_key"+dateString;
        String lockKey = "plan_add_lock"+dateString;

        //死锁，等待addKey释放
        while(true){
            Boolean lock = redisTemplate.opsForValue().setIfAbsent(lockKey,"锁定中",10, TimeUnit.SECONDS);
            if(lock != null && lock){
                if(!redisTemplate.hasKey(key)){
                    QueryWrapper<OperationPlan> queryWrapper = new QueryWrapper<>();
                    queryWrapper.apply("date(plan_departure_date) = {0}", operationPlan.getPlanDepartureDate());
                    queryWrapper.orderByDesc("create_time").last("limit 1");
                    OperationPlan temp = operationPlanService.getOne(queryWrapper);
                    if(temp != null){
                        String keyValue = temp.getTeamId().substring(prefix.length());
                        redisTemplate.opsForValue().set(key,keyValue);
                    }else {
                        redisTemplate.opsForValue().set(key,"0");
                    }
                }
                String team_no_source = String.valueOf(redisTemplate.opsForValue().get(key));
                Integer team_no = Integer.valueOf(team_no_source) + 1;
                redisTemplate.opsForValue().set(key,team_no);
                redisTemplate.delete(lockKey);

                //大于三位数会正常输出
                String team_id = prefix + String.format("%03d", team_no);
                operationPlan.setTeamId(team_id);
                return toAjax(operationPlanService.save(operationPlan));

            }
        }
    }

    /**
     * 修改操作计划
     */
    @ApiOperation("修改操作计划")
    @PreAuthorize("@ss.hasPermi('travel:plan:edit')")
    @Log(title = "操作计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OperationPlan operationPlan) {
        return toAjax(operationPlanService.updateById(operationPlan));
    }

    /**
     * 删除操作计划
     */
    @ApiOperation("删除操作计划")
    @PreAuthorize("@ss.hasPermi('travel:plan:remove')")
    @Log(title = "操作计划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        AjaxResult ajaxResult = toAjax(operationPlanService.removeByIds(Arrays.asList(ids)));
        for (Long id:ids){
            QueryWrapper<PlanDetail> queryWrapper = new QueryWrapper();
            queryWrapper.eq("operation_plan_id",id);
            planDetailService.remove(queryWrapper);

            QueryWrapper<CashDetail> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("related_id",id).eq("cash_type","plan");
            cashDetailService.remove(queryWrapper1);
        }
        return ajaxResult;
    }
}
