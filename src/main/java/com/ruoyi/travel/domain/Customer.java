package com.ruoyi.travel.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import java.io.Serializable;
import lombok.Data;
import com.ruoyi.framework.web.domain.MybatisPlusBaseEntity;

/**
 * 客户信息对象 travel_customer
 * 
 * @author 陈宇凡
 * @date 2023-05-05
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("travel_customer")
public class Customer extends MybatisPlusBaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    /** 客户信息ID */
    private Long id;


    private Long itineraryId;

    /** 收客记录ID */
    @Excel(name = "收客记录ID")
    private Long bookingId;

    /** 组团社ID */
    @Excel(name = "组团社ID")
    private Long groupId;

    /** 客户姓名 */
    @Excel(name = "客户姓名")
    @TableField(condition = SqlCondition.LIKE)
    private String customerName;

    /** 证件类型 */
    @Excel(name = "证件类型",dictType="travel_customer_idtype")
    private String customerIdType;

    /** 证件号码 */
    @Excel(name = "证件号码")
    @TableField(condition = SqlCondition.LIKE)
    private String customerIdNumber;

    /** 联系方式 */
    @Excel(name = "联系方式")
    @TableField(condition = SqlCondition.LIKE)
    private String customerContactInfo;

    /** 用房信息 */
    @Excel(name = "用房信息")
    private String customerRoomInfo;

}
