package com.nhnacademy.heukbaekbookshop.order.dto.response;

import java.util.List;

public record OrderResponse(
        List<OrderSummaryResponse> orderSummaryResponseList
) {
}
