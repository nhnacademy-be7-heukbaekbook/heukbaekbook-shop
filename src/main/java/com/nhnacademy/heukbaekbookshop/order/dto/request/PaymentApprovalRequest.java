package com.nhnacademy.heukbaekbookshop.order.dto.request;

public record PaymentApprovalRequest(
        Long orderId,
        String paymentKey,
        String paymentOrderId,
        Long amount
) {}
