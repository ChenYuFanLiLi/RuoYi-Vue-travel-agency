package com.ruoyi.travel.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.travel.mapper.CostAccountingMapper;
import com.ruoyi.travel.domain.CostAccounting;
import com.ruoyi.travel.service.ICostAccountingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 成本核算，用于记录团队成本核算信息Service业务层处理
 * 
 * @author 陈宇凡
 * @date 2023-05-21
 */
@Service
public class CostAccountingServiceImpl extends ServiceImpl<CostAccountingMapper, CostAccounting> implements ICostAccountingService {

    @Autowired
    private CostAccountingMapper costAccountingMapper;

    @Override
    public void calcTrends(Long operationCostId) {
        UpdateWrapper<CostAccounting> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("cost_reimbursement_detail = (SELECT SUM(cost_cash) from travel_cost_detail WHERE operation_cost_id = " + operationCostId + ")");
        updateWrapper.setSql("cost_total_transfer = (SELECT SUM(cash_amount) from travel_cash_detail WHERE related_id = " + operationCostId + " AND cash_type = 'cost')");
        updateWrapper.setSql("cost_guide_advance = cost_reimbursement_detail - cost_total_transfer");
        updateWrapper.eq("id", operationCostId);
        costAccountingMapper.update(null, updateWrapper);
    }

}
