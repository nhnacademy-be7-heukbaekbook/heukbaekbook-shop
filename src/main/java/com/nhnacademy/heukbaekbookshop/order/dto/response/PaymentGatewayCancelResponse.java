package com.nhnacademy.heukbaekbookshop.order.dto.response;

public record PaymentGatewayCancelResponse(
        String requestedAt,
        String approvedAt,
        String message
) {}
