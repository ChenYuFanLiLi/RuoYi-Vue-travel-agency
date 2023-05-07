package com.ruoyi.travel.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import java.io.Serializable;
import lombok.Data;
import com.ruoyi.framework.web.domain.MybatisPlusBaseEntity;

/**
 * 计划项目明细对象 travel_plan_detail
 * 
 * @author 陈宇凡
 * @date 2023-05-07
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("travel_plan_detail")
public class PlanDetail extends MybatisPlusBaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    /** 计划项目明细ID */
    private Long id;

    /** 操作计划ID */
    @Excel(name = "操作计划ID")
    private Long operationPlanId;

    /** 行程详情ID */
    @Excel(name = "行程详情ID")
    private Long travelScheduleId;

    /** 项目ID */
    private Long projectId;

    /** 类型 */
    @Excel(name = "类型")
    private String planType;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 项目成本 */
    @Excel(name = "项目成本")
    private BigDecimal projectCost;

    /** 项目数量 */
    @Excel(name = "项目数量")
    private BigDecimal projectQuantity;

    /** 项目单位 */
    @Excel(name = "项目单位")
    private String projectUnit;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal planAmount;

    /** 现金 */
    @Excel(name = "现金")
    private BigDecimal planCash;

    /** 刷卡 */
    @Excel(name = "刷卡")
    private BigDecimal planCard;

    /** 转账 */
    @Excel(name = "转账")
    private BigDecimal planTransfer;

    /** 冲抵 */
    @Excel(name = "冲抵")
    private BigDecimal planOffset;

    /** 挂账 */
    @Excel(name = "挂账")
    private BigDecimal planCredit;

}
