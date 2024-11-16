package com.nhnacademy.heukbaekbookshop.order.dto.response;

public record PaymentApprovalResponse(
        String code,
        boolean success,
        String message
) {}
