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
 * 现金明细对象 travel_cash_detail
 * 
 * @author 陈宇凡
 * @date 2023-05-30
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("travel_cash_detail")
public class CashDetail extends MybatisPlusBaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 关联ID */
    @Excel(name = "关联ID")
    private Long relatedId;

    /** 现收项目 */
    @Excel(name = "现收项目")
    private String cashProject;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal cashUnitPrice;

    /** 单位 */
    @Excel(name = "单位")
    private String cashUnit;

    /** 数量 */
    @Excel(name = "数量")
    private Long cashQuantity;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal cashAmount;

    /** 抵现 */
    @Excel(name = "抵现")
    private BigDecimal cashDiscount;

    /** 类型（操作计划，成本核算） */
    @Excel(name = "类型", readConverterExp = "操=作计划，成本核算")
    private String cashType;

}
