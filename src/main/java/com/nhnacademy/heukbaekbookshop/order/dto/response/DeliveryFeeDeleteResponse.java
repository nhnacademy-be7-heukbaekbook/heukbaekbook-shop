package com.nhnacademy.heukbaekbookshop.order.dto.response;

import java.math.BigDecimal;

public record DeliveryFeeDeleteResponse(
        Long id,
        String name,
        BigDecimal fee,
        BigDecimal minimumOrderAmount
) {}
