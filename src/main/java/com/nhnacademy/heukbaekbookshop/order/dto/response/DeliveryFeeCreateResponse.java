package com.nhnacademy.heukbaekbookshop.order.dto.response;

import java.math.BigDecimal;

public record DeliveryFeeCreateResponse(
        String name,
        BigDecimal fee,
        BigDecimal minimumOrderAmount
) {}
