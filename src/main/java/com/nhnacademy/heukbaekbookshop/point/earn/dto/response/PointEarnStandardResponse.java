package com.nhnacademy.heukbaekbookshop.point.dto.response;

import com.nhnacademy.heukbaekbookshop.point.domain.earn.PointEarnStandardStatus;
import com.nhnacademy.heukbaekbookshop.point.domain.PointEarnTriggerEvent;

import java.math.BigDecimal;

public record PointEarnStandardResponse(
        Long id,
        String name,
        BigDecimal point,
        PointEarnStandardStatus status,
        PointEarnTriggerEvent triggerEvent
) {
}
