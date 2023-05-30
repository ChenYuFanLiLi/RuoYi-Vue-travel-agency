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
 * 操作计划对象 travel_operation_plan
 * 
 * @author 陈宇凡
 * @date 2023-05-07
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("travel_operation_plan")
public class OperationPlan extends MybatisPlusBaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 行程ID */
    @Excel(name = "行程ID")
    private Long itineraryId;

    /** 团队编号 */
    @Excel(name = "团队编号")
    private String teamId;

    /** 团号 */
    @Excel(name = "团号")
    private String groupNumber;

    /** 出发日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出发日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planDepartureDate;

    /** 导游姓名 */
    @Excel(name = "导游姓名")
    private String guideName;

    /** 导游电话 */
    @Excel(name = "导游电话")
    private String guidePhone;

    /** 司机电话 */
    @Excel(name = "司机电话")
    private String driverPhone;

    /** 司机姓名 */
    @Excel(name = "司机姓名")
    private String driverName;

    /** 车牌号码 */
    @Excel(name = "车牌号码")
    private String carNumber;

    /** 计调姓名 */
    @Excel(name = "计调姓名")
    private String plannerName;

    /** 计调电话 */
    @Excel(name = "计调电话")
    private String plannerPhone;


    /** 现金合计 */
    @Excel(name = "现金合计")
    private BigDecimal planItemTotal;

    /** 现收合计 */
    @Excel(name = "现收合计")
    private BigDecimal planCashTotal;

    /** 导游领现 */
    @Excel(name = "导游领现")
    private BigDecimal planGuideCash;


}
