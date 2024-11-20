package com.nhnacademy.heukbaekbookshop.order.dto.response;

public record PaymentDetailResponse(
        Long paymentId,
        String paymentType,
        String requestedAt,
        String approvedAt,
        Long amount
) {}
