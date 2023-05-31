package com.ruoyi.travel.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.travel.domain.CostAccounting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.travel.mapper.OperationPlanMapper;
import com.ruoyi.travel.domain.OperationPlan;
import com.ruoyi.travel.service.IOperationPlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 操作计划Service业务层处理
 * 
 * @author 陈宇凡
 * @date 2023-05-07
 */
@Service
public class OperationPlanServiceImpl extends ServiceImpl<OperationPlanMapper, OperationPlan> implements IOperationPlanService {
    @Autowired
    private OperationPlanMapper operationPlanMapper;

    @Override
    public void calcTrends(Long operationPlanId) {
        UpdateWrapper<OperationPlan> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("plan_item_total = (SELECT SUM(plan_cash) from travel_plan_detail WHERE operation_plan_id = " + operationPlanId + ")");
        updateWrapper.setSql("plan_cash_total = (SELECT SUM(cash_amount) from travel_cash_detail WHERE related_id = " + operationPlanId + " AND cash_type = 'plan')");
        updateWrapper.setSql("plan_guide_cash = plan_item_total - plan_cash_total");
        updateWrapper.eq("id", operationPlanId);
        operationPlanMapper.update(null, updateWrapper);
    }
}
