package com.nhnacademy.heukbaekbookshop.order.dto.request;

public record PaymentCancelRequest(
        String paymentKey,
        String cancelReason,
        String method
) {}
