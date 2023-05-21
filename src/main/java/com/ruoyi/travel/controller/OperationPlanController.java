package com.ruoyi.travel.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.travel.domain.Itinerary;
import com.ruoyi.travel.service.IItineraryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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
import com.ruoyi.travel.domain.OperationPlan;
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
    @ApiOperation("导出操作计划列表")
    @PreAuthorize("@ss.hasPermi('travel:plan:export')")
    @Log(title = "操作计划", businessType = BusinessType.EXPORT)
    @RequestMapping("/export")
    public void export(HttpServletResponse response,OperationPlan operationPlan) {
        List<OperationPlan> list = operationPlanService.list(new QueryWrapper<OperationPlan>(operationPlan));
        ExcelUtil<OperationPlan> util = new ExcelUtil<OperationPlan>(OperationPlan.class);
        util.exportExcel(response ,list, "plan");
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
                        redisTemplate.opsForValue().set(key,Integer.valueOf(keyValue));
                    }else {
                        redisTemplate.opsForValue().set(key,0);
                    }
                }
                String team_no_source = (String) redisTemplate.opsForValue().get(key);
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
        return toAjax(operationPlanService.removeByIds(Arrays.asList(ids)));
    }
}
