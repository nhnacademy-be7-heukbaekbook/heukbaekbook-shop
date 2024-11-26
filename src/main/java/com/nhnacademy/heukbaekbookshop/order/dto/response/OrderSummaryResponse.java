package com.nhnacademy.heukbaekbookshop.order.dto.response;

public record OrderSummaryResponse(
        String createdAt,
        String tossOrderId,
        String title,
        String status,
        DeliverySummaryResponse deliverySummaryResponse
) {
}
