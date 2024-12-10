package com.nhnacademy.heukbaekbookshop.order.dto.response;

import org.springframework.data.domain.Page;


public record OrderResponse(
        Page<OrderSummaryResponse> orderSummaryResponsePage
) {
}
