package com.ruoyi.travel.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
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

}
