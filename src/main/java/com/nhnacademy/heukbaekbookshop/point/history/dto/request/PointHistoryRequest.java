package com.nhnacademy.heukbaekbookshop.point.history.dto.request;

import com.nhnacademy.heukbaekbookshop.point.history.domain.PointType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PointHistoryRequest(
        @NotNull
        Long customerId,

        Long orderId,

        @NotNull
        @Min(1)
        BigDecimal amount,

        @NotNull
        LocalDateTime createdAt,

        @NotNull
        PointType type
) {
}
