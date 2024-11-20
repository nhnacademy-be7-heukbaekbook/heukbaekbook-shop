package com.nhnacademy.heukbaekbookshop.order.dto.response;

public record TossPaymentCancelResponse(
        String requestedAt,
        String approvedAt
) {}
