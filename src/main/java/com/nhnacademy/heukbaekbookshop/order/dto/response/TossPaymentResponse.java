package com.nhnacademy.heukbaekbookshop.order.dto.response;

public record TossPaymentResponse(
        String mId,
        String paymentKey,
        String orderId,
        String orderName,
        String method,
        String requestedAt,
        String approvedAt,
        Long totalAmount
) {}
