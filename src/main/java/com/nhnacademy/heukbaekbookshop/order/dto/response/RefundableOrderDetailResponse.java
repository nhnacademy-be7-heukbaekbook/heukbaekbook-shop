package com.nhnacademy.heukbaekbookshop.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record RefundableOrderDetailResponse(
        Long orderId,
        String customerName,
        String deliveryFee,
        String paymentPrice,
        String paymentTypeName,
        String recipient,
        Long postalCode,
        String roadNameAddress,
        String detailAddress,
        String totalBookPrice,
        String totalBookDiscountPrice,
        String totalPrice,
        List<RefundableOrderBookResponse> books,
        LocalDateTime createdAt,
        String status,
        String paymentKey
) {}
