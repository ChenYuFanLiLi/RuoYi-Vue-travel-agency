package com.ruoyi.travel.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import java.io.Serializable;
import lombok.Data;
import com.ruoyi.framework.web.domain.MybatisPlusBaseEntity;

/**
 * 收客记录对象 travel_booking
 * 
 * @author 陈宇凡
 * @date 2023-05-05
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("travel_booking")
public class Booking extends MybatisPlusBaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    /** 收客记录ID */
    private Long id;

    /** 收客信息ID */
    @Excel(name = "收客信息ID")
    private Long bookingInfoId;

    /** 行程ID */
    @Excel(name = "行程ID")
    private Long itineraryId;

    /** 组团社ID */
    @Excel(name = "组团社ID")
    private Long groupId;

    /** 留位人ID */
    @Excel(name = "留位人ID")
    private Long bookerId;

    /** 预定数量 */
    @Excel(name = "预定数量")
    private Long bookingCount;

    /** 组团社名称 */
    @Excel(name = "组团社名称")
    @TableField(condition = SqlCondition.LIKE)
    private String groupName;

    /** 组团社负责人姓名 */
    @Excel(name = "组团社负责人姓名")
    @TableField(condition = SqlCondition.LIKE)
    private String groupLeaderName;

    /** 组团社负责人电话 */
    @Excel(name = "组团社负责人电话")
    @TableField(condition = SqlCondition.LIKE)
    private String groupLeaderPhone;

}
