package com.nhnacademy.heukbaekbookshop.point.earn.dto.response;

import com.nhnacademy.heukbaekbookshop.point.earn.domain.EventCode;
import com.nhnacademy.heukbaekbookshop.point.earn.domain.PointEarnStandardStatus;
import com.nhnacademy.heukbaekbookshop.point.earn.domain.PointEarnType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PointEarnStandardResponse(
        Long id,
        String name,
        BigDecimal point,
        PointEarnStandardStatus status,
        PointEarnType pointEarnType,
        LocalDateTime pointEarnStart,
        LocalDateTime pointEarnEnd,
        EventCode eventCode
) {
}
