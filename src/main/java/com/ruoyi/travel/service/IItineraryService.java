package com.ruoyi.travel.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.travel.domain.Itinerary;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.travel.vo.ItineraryVO;

import java.util.List;

/**
 * 行程Service接口
 * 
 * @author 陈宇凡
 * @date 2023-05-05
 */
public interface IItineraryService extends IService<Itinerary> {

    List<ItineraryVO> listVO(QueryWrapper<Itinerary> itineraryQueryWrapper);
}
