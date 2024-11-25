package com.nhnacademy.heukbaekbookshop.order.dto.response;

import java.math.BigDecimal;

public record DeliveryFeeDetailResponse(
        Long id,
        String name,
        BigDecimal fee,
        BigDecimal minimumOrderAmount
) {}
