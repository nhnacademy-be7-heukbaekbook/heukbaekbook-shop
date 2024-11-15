package com.nhnacademy.heukbaekbookshop.point.dto.request;

import com.nhnacademy.heukbaekbookshop.point.domain.earn.PointEarnStandardStatus;
import com.nhnacademy.heukbaekbookshop.point.domain.PointEarnTriggerEvent;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public record PointEarnStandardRequest(
        @NotNull
        @Length(max = 20)
        String name,

        @Min(0)
        @NotNull
        BigDecimal point,

        @NotNull
        PointEarnStandardStatus status,

        @NotNull
        PointEarnTriggerEvent triggerEvent
) {
}
