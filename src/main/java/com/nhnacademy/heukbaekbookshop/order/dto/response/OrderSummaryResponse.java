package com.nhnacademy.heukbaekbookshop.order.dto.response;

import java.time.LocalDate;

public record OrderSummaryResponse(
        LocalDate createdAt,
        String tossOrderId,
        String status,
        DeliverySummaryResponse deliverySummaryResponse
) {
}
