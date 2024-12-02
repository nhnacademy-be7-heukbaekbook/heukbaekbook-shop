package com.nhnacademy.heukbaekbookshop.order.dto.request;

public record PaymentApprovalRequest(
        String paymentKey,
        String orderId,
        Long amount,
        String method
) {}
