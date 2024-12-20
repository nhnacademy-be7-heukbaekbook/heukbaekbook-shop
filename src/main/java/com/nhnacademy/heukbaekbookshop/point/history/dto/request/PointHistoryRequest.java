package com.nhnacademy.heukbaekbookshop.point.history.dto.request;

import com.nhnacademy.heukbaekbookshop.point.history.domain.PointType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PointHistoryRequest(
        Long orderId,

        @NotNull
        @Size(min = 1, max = 20)
        String pointName,

        @NotNull
        @Min(1)
        BigDecimal amount,

        @NotNull
        LocalDateTime createdAt,

        @NotNull
        PointType type
) {
}
