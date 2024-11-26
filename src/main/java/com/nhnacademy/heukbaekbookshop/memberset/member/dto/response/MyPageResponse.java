package com.nhnacademy.heukbaekbookshop.memberset.member.dto.response;

import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderResponse;

public record MyPageResponse(
        MemberResponse memberResponse,
        OrderResponse orderResponse
) {
}
