package com.nhnacademy.heukbaekbookshop.point.history.event;

import java.math.BigDecimal;

public record PointUseEvent(
        Long customerId,
        Long orderId,
        BigDecimal usePointAmount
) {
}
