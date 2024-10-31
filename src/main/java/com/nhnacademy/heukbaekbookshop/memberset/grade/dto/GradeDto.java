package com.nhnacademy.heukbaekbookshop.memberset.grade.dto;

import java.math.BigDecimal;

public record GradeDto (
        String gradeName,
        BigDecimal pointPercentage,
        BigDecimal promotionStandard
) {
}
