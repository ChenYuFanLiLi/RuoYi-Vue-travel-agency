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
 * 成本核算明细对象 travel_cost_detail
 * 
 * @author 陈宇凡
 * @date 2023-05-21
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("travel_cost_detail")
public class CostDetail extends MybatisPlusBaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 计划项目 */
    @Excel(name = "计划项目")
    private Long planDetailId;

    /** 行程ID */
    private Long travelScheduleId;

    /** 项目ID */
    @Excel(name = "项目ID")
    private String projectId;

    /** 项目类型 */
    @Excel(name = "项目类型")
    private String planType;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 项目成本 */
    @Excel(name = "项目成本")
    private BigDecimal projectCost;

    /** 项目数量 */
    @Excel(name = "项目数量")
    private BigDecimal projectAmount;

    /** 项目单位 */
    @Excel(name = "项目单位")
    private String projectUnit;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal costAmount;

    /** 现金 */
    @Excel(name = "现金")
    private BigDecimal costCash;

    /** 刷卡 */
    @Excel(name = "刷卡")
    private BigDecimal costCard;

    /** 转账 */
    @Excel(name = "转账")
    private BigDecimal costTransfer;

    /** 冲抵 */
    @Excel(name = "冲抵")
    private BigDecimal costOffset;

    /** 挂账 */
    @Excel(name = "挂账")
    private BigDecimal costOnCredit;

    /** 凭据 */
    @Excel(name = "凭据")
    private String costVoucher;

}
