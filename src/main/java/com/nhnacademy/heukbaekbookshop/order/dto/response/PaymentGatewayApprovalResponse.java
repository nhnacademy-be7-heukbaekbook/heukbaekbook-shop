package com.nhnacademy.heukbaekbookshop.order.dto.response;

import java.math.BigDecimal;

public record PaymentGatewayApprovalResponse(
        String paymentKey,
        String requestedAt,
        String approvedAt,
        BigDecimal cancelAmount,
        String method
) {}
