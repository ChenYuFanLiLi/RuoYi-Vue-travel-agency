package com.ruoyi.travel.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.travel.domain.Booking;
import com.ruoyi.travel.domain.Group;
import com.ruoyi.travel.dto.CustomerDTO;
import com.ruoyi.travel.service.IBookingService;
import com.ruoyi.travel.service.IGroupService;
import com.ruoyi.travel.vo.CustomerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.travel.mapper.CustomerMapper;
import com.ruoyi.travel.domain.Customer;
import com.ruoyi.travel.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 客户信息Service业务层处理
 * 
 * @author 陈宇凡
 * @date 2023-05-05
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Autowired
    private IBookingService bookingService;

    @Autowired
    private IGroupService groupService;

    @Override
    public String importCustomer(List<Customer> customerList) {
        boolean save = saveBatch(customerList);
        if (save){
            return "导入成功";
        }else {
            return "导入失败";
        }
    }

//    @Override
//    public List<CustomerVO> listByItineraryId(CustomerDTO customerDTO) {
//
//        return list;
//    }
}
