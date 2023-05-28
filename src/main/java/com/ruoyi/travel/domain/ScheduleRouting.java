package com.ruoyi.travel.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.framework.web.domain.MybatisPlusBaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.io.Serializable;

/**
 * 线路安排对象 travel_schedule_routing
 * 
 * @author 陈宇凡
 * @date 2023-05-28
 */
@Data
@TableName("travel_schedule_routing")
public class ScheduleRouting extends MybatisPlusBaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 线路ID */
    @Excel(name = "线路ID")
    private Long scheduleId;

    /** 行程安排内容 */
    @Excel(name = "行程安排内容")
    private String routingContent;

    /** 行程安排顺序（第几天） */
    @Excel(name = "行程安排顺序", readConverterExp = "第=几天")
    private Long routingOrder;

    /** 住宿地 */
    @Excel(name = "住宿地")
    private String routingPlace;

    /** 餐饮 */
    @Excel(name = "餐饮")
    private String routingMeal;

}
