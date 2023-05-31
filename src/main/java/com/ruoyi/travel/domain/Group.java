package com.ruoyi.travel.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.framework.web.domain.MybatisPlusBaseEntity;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import java.io.Serializable;
import lombok.Data;

/**
 * 组团社信息对象 travel_group
 * 
 * @author 陈宇凡
 * @date 2023-05-04
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("travel_group")
public class Group extends MybatisPlusBaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    /** 组团社ID */
//    @TableId(type = IdType.AUTO)
    private Long id;

    /** 组团社名称 */
    @Excel(name = "组团社名称")
    @TableField(condition = SqlCondition.LIKE)
    private String groupName;

    /** 组团社负责人姓名 */
    @Excel(name = "组团社负责人姓名")
    private String groupLeaderName;

    /** 组团社负责人电话 */
    @Excel(name = "组团社负责人电话")
    private String groupLeaderPhone;

    /** 组团社公司名称 */
    @Excel(name = "组团社公司名称")
    private String groupCompanyName;

    /** 组团社传真号码 */
    @Excel(name = "组团社传真号码")
    private String groupFax;

    /** 组团社电话号码 */
    @Excel(name = "组团社电话号码")
    private String groupPhone;

    /** 组团社手机号码 */
    @Excel(name = "组团社手机号码")
    private String groupMobile;

    /** 组团社收件人 */
    @Excel(name = "组团社收件人")
    private String groupRecipient;

}
