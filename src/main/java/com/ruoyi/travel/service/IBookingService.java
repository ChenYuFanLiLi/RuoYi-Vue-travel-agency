package com.ruoyi.travel.service;

import com.ruoyi.travel.domain.Booking;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 收客记录Service接口
 * 
 * @author 陈宇凡
 * @date 2023-05-05
 */
public interface IBookingService extends IService<Booking> {

    List<Booking> listByItineraryId(Long itineraryId);
}
