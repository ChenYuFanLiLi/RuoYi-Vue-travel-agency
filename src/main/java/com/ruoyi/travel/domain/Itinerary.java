package com.ruoyi.travel.domain;

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
 * 行程对象 travel_itinerary
 * 
 * @author 陈宇凡
 * @date 2023-05-05
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("travel_itinerary")
public class Itinerary extends MybatisPlusBaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    /** 行程ID */
    private Long id;

    /** 行程名称 */
    @Excel(name = "行程名称")
    @TableField(condition = SqlCondition.LIKE)
    private String itineraryName;

    /** 行程简称 */
    @Excel(name = "行程简称")
    @TableField(condition = SqlCondition.LIKE)
    private String itineraryShortName;

    /** 线路名称 */
    @Excel(name = "线路名称")
    private String scheduleName;

    /** 线路天数 */
    @Excel(name = "线路天数")
    private Long scheduleDays;

    /** 线路说明 */
    @Excel(name = "线路说明")
    private String scheduleDescription;

    /** 行程安排 */
    @Excel(name = "行程安排")
    private String itinerarySchedule;

    /** 计划数量 */
    @Excel(name = "计划数量")
    private Long planQuantity;

    /** 发团日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发团日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date departureDate;

    /** 收客社简述 */
    @Excel(name = "收客社简述")
    private String clientBrief;

    /** 行程文档 */
    @Excel(name = "行程文档")
    private String itineraryDocument;

}
