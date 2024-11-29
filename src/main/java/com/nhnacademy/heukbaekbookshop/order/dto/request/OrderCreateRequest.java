package com.nhnacademy.heukbaekbookshop.order.dto.request;

import java.util.List;

public record OrderCreateRequest(
        Long customerId,
        String totalPrice,
        String customerEmail,
        String customerName,
        String customerPhoneNumber,
        String tossOrderId,
        String recipient,
        String postalCode,
        String roadNameAddress,
        String detailAddress,
        String recipientPhoneNumber,
        String deliveryFee,
        String usedPoint,
        List<OrderBookRequest> orderBookRequests) {
}
