package com.ruoyi.travel.vo;

import com.ruoyi.travel.domain.Itinerary;
import lombok.Data;

/**
 * @author EMPTATION_TOFFEET
 */
@Data
public class ItineraryVO extends Itinerary {
    /** 占位预留 */
    private Long itineraryObligate;

    /** 确认或有名单人数 */
    private Long itineraryConfirm;

}
