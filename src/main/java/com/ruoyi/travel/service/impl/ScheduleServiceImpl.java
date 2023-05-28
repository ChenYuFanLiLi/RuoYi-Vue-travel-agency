package com.ruoyi.travel.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.travel.domain.ScheduleRouting;
import com.ruoyi.travel.mapper.ScheduleMapper;
import com.ruoyi.travel.domain.Schedule;
import com.ruoyi.travel.service.IScheduleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 线路信息管理Service业务层处理
 * 
 * @author 陈宇凡
 * @date 2023-05-28
 */
@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements IScheduleService {

}
