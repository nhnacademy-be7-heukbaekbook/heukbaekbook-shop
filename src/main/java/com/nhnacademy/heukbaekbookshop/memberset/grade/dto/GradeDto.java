package com.nhnacademy.heukbaekbookshop.memberset.grade.dto;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public record GradeDto (
        String gradeName,
        BigDecimal pointPercentage,
        BigDecimal promotionStandard
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
