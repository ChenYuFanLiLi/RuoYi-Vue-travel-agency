package com.ruoyi.travel.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.MybatisPlusBaseEntity;
import com.ruoyi.travel.domain.Customer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

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
public class CustomerVO extends Customer implements Serializable{
    private String groupName;

    public CustomerVO(Customer customer){
        this.setId(customer.getId());
        this.setItineraryId(customer.getItineraryId());
        this.setBookingId(customer.getBookingId());
        this.setGroupId(customer.getGroupId());
        this.setCustomerName(customer.getCustomerName());
        this.setCustomerIdType(customer.getCustomerIdType());
        this.setCustomerIdNumber(customer.getCustomerIdNumber());
        this.setCustomerContactInfo(customer.getCustomerContactInfo());
        this.setCustomerRoomInfo(customer.getCustomerRoomInfo());
        this.setRemark(customer.getRemark());
    }
}
