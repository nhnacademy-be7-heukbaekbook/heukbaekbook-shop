package com.nhnacademy.heukbaekbookshop.order.dto.response;

public record PaymentApprovalResponse(
        String orderCreatedAt,
        String orderStatus,
        String orderCustomerName,
        String orderCustomerPhoneNumber,
        String orderCustomerEmail,
        String paymentType,
        String paymentRequestedAt,
        String paymentApprovedAt,
        int paymentPrice,
        String message
) {}
