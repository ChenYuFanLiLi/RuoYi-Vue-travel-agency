package com.ruoyi.travel.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.travel.mapper.BookingMapper;
import com.ruoyi.travel.domain.Booking;
import com.ruoyi.travel.service.IBookingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 收客记录Service业务层处理
 * 
 * @author 陈宇凡
 * @date 2023-05-05
 */
@Service
public class BookingServiceImpl extends ServiceImpl<BookingMapper, Booking> implements IBookingService {

    /**
     * 通过行程ID 查询收客记录
     * @param itineraryId 行程ID
     * @return 收客记录
     */
    @Override
    public List<Booking> listByItineraryId(Long itineraryId) {
        QueryWrapper<Booking> bookingQueryWrapper = new QueryWrapper<Booking>().eq("itinerary_id",itineraryId);
        return list(bookingQueryWrapper);
    }

    @Override
    public Map<Long, List<Booking>> listByItineraryIdsToMap(List<Long> itineraryIds) {
        return listByItineraryIds(itineraryIds).stream().collect(Collectors.groupingBy(Booking::getItineraryId));
    }

    @Override
    public List<Booking> listByItineraryIds(List<Long> itineraryIds) {
        QueryWrapper<Booking> bookingQueryWrapper = new QueryWrapper<Booking>().in("itinerary_id", itineraryIds);
        return list(bookingQueryWrapper);
    }
}
