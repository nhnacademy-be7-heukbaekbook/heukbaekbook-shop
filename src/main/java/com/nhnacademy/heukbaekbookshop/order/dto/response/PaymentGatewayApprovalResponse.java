package com.nhnacademy.heukbaekbookshop.order.dto.response;

public record PaymentGatewayApprovalResponse(
        String paymentKey,
        String requestedAt,
        String approvedAt,
        int totalAmount,
        String method
) {}
