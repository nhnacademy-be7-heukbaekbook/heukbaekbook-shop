package com.nhnacademy.heukbaekbookshop.order.dto.response;

public record PaymentDetailResponse(
        String paymentId,
        String paymentType,
        String requestedAt,
        String approvedAt,
        Long amount
) {}
