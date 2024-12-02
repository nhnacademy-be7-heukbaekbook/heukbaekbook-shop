package com.nhnacademy.heukbaekbookshop.order.dto.response;

import java.math.BigDecimal;

public record TossPaymentCancelResponse(
        String requestedAt,
        String approvedAt,
        String orderId,
        BigDecimal cancelAmount
) {}
