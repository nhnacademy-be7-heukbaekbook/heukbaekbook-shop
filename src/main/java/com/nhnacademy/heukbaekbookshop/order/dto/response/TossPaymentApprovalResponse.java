package com.nhnacademy.heukbaekbookshop.order.dto.response;

public record TossPaymentApprovalResponse(
        String paymentKey,
        String requestedAt,
        String approvedAt,
        int paymentPrice,
        String method
) {}
