package com.nhnacademy.heukbaekbookshop.order.dto.request;

import java.math.BigDecimal;

public record DeliveryFeeCreateRequest(
        String name,
        BigDecimal fee,
        BigDecimal minimumOrderAmount
) {}
