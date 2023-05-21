package com.ruoyi.travel.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.*;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import java.io.Serializable;
import lombok.Data;
import com.ruoyi.framework.web.domain.MybatisPlusBaseEntity;

/**
 * 成本核算，用于记录团队成本核算信息对象 travel_cost_accounting
 * 
 * @author 陈宇凡
 * @date 2023-05-21
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("travel_cost_accounting")
public class CostAccounting extends MybatisPlusBaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 操作计划 */
    @Excel(name = "操作计划")
    private Long operationPlanId;

    @Excel(name = "行程")
    private Long itineraryId;

    /** 团号 */
    @Excel(name = "团号")
    private String tourNumber;

    /** 出发日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出发日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date departureDate;

    /** 团队编号 */
    @Excel(name = "团队编号")
    private String teamNumber;

    /** 合计转出 */
    @Excel(name = "合计转出")
    private BigDecimal costTotalTransfer;

    /** 导游垫款 */
    @Excel(name = "导游垫款")
    private BigDecimal costGuideAdvance;

    /** 报账明细 */
    @Excel(name = "报账明细")
    private String costReimbursementDetail;

}
