package com.nhnacademy.heukbaekbookshop.point.history.event;

import java.math.BigDecimal;


public record OrderEvent(
        Long customerId,
        Long orderId,
        BigDecimal orderAmount
) {
}
