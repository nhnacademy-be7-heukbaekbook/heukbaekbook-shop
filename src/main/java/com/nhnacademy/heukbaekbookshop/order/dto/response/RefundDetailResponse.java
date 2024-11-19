package com.nhnacademy.heukbaekbookshop.order.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record RefundDetailResponse(
        Long refundId,
        String reason,
        String requestedAt,
        String approvedAt,
        String returnStatus,
        Long orderId,
        List<String> titles,
        List<Integer> quantities,
        List<BigDecimal> prices
) {}
