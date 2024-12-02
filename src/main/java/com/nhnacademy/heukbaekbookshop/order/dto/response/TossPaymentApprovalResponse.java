package com.nhnacademy.heukbaekbookshop.order.dto.response;

import java.math.BigDecimal;

public record TossPaymentApprovalResponse(
        String paymentKey,
        String requestedAt,
        String approvedAt,
        BigDecimal totalAmount,
        String method
) {}
