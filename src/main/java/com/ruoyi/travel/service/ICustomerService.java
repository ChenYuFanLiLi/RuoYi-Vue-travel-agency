package com.ruoyi.travel.service;

import com.ruoyi.travel.domain.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.travel.dto.CustomerDTO;
import com.ruoyi.travel.vo.CustomerVO;

import java.util.List;
import java.util.Map;

/**
 * 客户信息Service接口
 * 
 * @author 陈宇凡
 * @date 2023-05-05
 */
public interface ICustomerService extends IService<Customer> {

    String importCustomer(List<Customer> customerList);

    public Map<Long, List<Customer>> listByBookingIdsToMap(List<Long> bookingIds);

    public List<Customer> listByBookingIds(List<Long> bookingIds);


}
