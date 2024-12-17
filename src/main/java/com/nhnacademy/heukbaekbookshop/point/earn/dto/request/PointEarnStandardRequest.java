package com.nhnacademy.heukbaekbookshop.point.earn.dto.request;

import com.nhnacademy.heukbaekbookshop.point.earn.domain.PointEarnStandardStatus;
import com.nhnacademy.heukbaekbookshop.point.earn.domain.PointEarnType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PointEarnStandardRequest(
        @NotNull
        @Length(max = 20)
        String name,

        @Min(0)
        @NotNull
        BigDecimal point,

        @NotNull
        PointEarnType pointEarnType,

        @NotNull
        PointEarnStandardStatus status,

        @NotNull
        LocalDateTime pointEarnStart,

        LocalDateTime pointEarnEnd,

        @NotNull
        Long pointEarnEventId
) {
}
