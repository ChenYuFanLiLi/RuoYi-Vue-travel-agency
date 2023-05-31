package com.ruoyi.travel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.travel.domain.ScheduleRouting;

import java.util.List;

public interface IScheduleRoutingService extends IService<ScheduleRouting> {

    Boolean removeByScheduleIds(Long[] ids);

    List<ScheduleRouting> listByScheduleId(Long id);
}
