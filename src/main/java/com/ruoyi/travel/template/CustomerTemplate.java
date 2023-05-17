package com.ruoyi.travel.template;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.MybatisPlusBaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 客户信息对象 travel_customer
 * 
 * @author 陈宇凡
 * @date 2023-05-05
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CustomerTemplate implements Serializable{
    /** 客户姓名 */
    @Excel(name = "客户姓名")
    private String customerName;

    /** 证件类型 */
    @Excel(name = "证件类型",dictType = "travel_customer_idtype")
    private String customerIdType;

    /** 证件号码 */
    @Excel(name = "证件号码")
    private String customerIdNumber;

    /** 联系方式 */
    @Excel(name = "联系方式")
    private String customerContactInfo;

    /** 用房信息 */
    @Excel(name = "用房信息")
    private String customerRoomInfo;

    @Excel(name = "备注")
    private String remark;

}
