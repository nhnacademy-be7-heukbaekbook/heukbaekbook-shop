package com.nhnacademy.heukbaekbookshop.order.dto.response;

import java.math.BigDecimal;

public record DeliveryFeeUpdateResponse(
        String name,
        BigDecimal fee,
        BigDecimal minimumOrderAmount
) {}
