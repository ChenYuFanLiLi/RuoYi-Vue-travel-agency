package com.ruoyi.travel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.travel.domain.ScheduleRouting;

public interface IScheduleRoutingService extends IService<ScheduleRouting> {

    Boolean removeByScheduleIds(Long[] ids);
}
