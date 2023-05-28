package com.ruoyi.travel.vo;

import com.ruoyi.travel.domain.Booking;
import lombok.Data;

@Data
public class BookingVO extends Booking {
    private Long customerCount;
}
