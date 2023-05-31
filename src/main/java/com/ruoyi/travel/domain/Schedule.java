package com.ruoyi.travel.domain;

import java.math.BigDecimal;
import java.util.List;
import com.baomidou.mybatisplus.annotation.*;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import java.io.Serializable;
import lombok.Data;
import com.ruoyi.framework.web.domain.MybatisPlusBaseEntity;

/**
 * 线路信息管理对象 travel_schedule
 * 
 * @author 陈宇凡
 * @date 2023-05-28
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("travel_schedule")
public class Schedule extends MybatisPlusBaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    /** 线路ID */
    private Long id;

    /** 线路名称 */
    @Excel(name = "线路名称")
    @TableField(condition = SqlCondition.LIKE)
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

    /** 行程顺序 */
    @Excel(name = "行程顺序")
    private Long itineraryOrder;

    /** 住宿地 */
    @Excel(name = "住宿地")
    private String accommodationPlace;

    /** 餐饮 */
    @Excel(name = "餐饮")
    private String mealInfo;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal price;

}
