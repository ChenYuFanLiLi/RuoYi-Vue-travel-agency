package com.ruoyi.travel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.travel.domain.ScheduleRouting;
import com.ruoyi.travel.mapper.ScheduleRoutingMapper;
import com.ruoyi.travel.service.IScheduleRoutingService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleRoutingServiceImpl extends ServiceImpl<ScheduleRoutingMapper, ScheduleRouting> implements IScheduleRoutingService {

    @Override
    public boolean updateBatchById(Collection<ScheduleRouting> entityList) {
        QueryWrapper<ScheduleRouting> scheduleRoutingQueryWrapper = new QueryWrapper<>();
        scheduleRoutingQueryWrapper.in("schedule_id",entityList.stream().map(ScheduleRouting::getScheduleId).collect(Collectors.toList()));
        List<ScheduleRouting> list = list(scheduleRoutingQueryWrapper);
        List<ScheduleRouting> removeBatchList = list.stream().filter(item -> !entityList.stream().map(ScheduleRouting::getId).collect(Collectors.toList()).contains(item.getId())).collect(Collectors.toList());
        List<ScheduleRouting> updateBatchList = entityList.stream().filter(item -> item.getId() != null).collect(Collectors.toList());
        List<ScheduleRouting> insertBatchList = entityList.stream().filter(item -> item.getId() == null).collect(Collectors.toList());
        if (insertBatchList.size()>0){
            boolean insertBoolean = saveBatch(insertBatchList);
        }
        if (removeBatchList.size()>0){
            boolean removeBoolean = removeByIds(removeBatchList.stream().map(ScheduleRouting::getId).collect(Collectors.toList()));
        }
        return super.updateBatchById(updateBatchList);
    }

    @Override
    public Boolean removeByScheduleIds(Long[] ids) {
        QueryWrapper<ScheduleRouting>scheduleRoutingQueryWrapper = new QueryWrapper<>();
        scheduleRoutingQueryWrapper.in("schedule_id",ids);
        List<ScheduleRouting> list = list(scheduleRoutingQueryWrapper);
        return removeByIds(list.stream().map(ScheduleRouting::getId).collect(Collectors.toList()));
    }
}
