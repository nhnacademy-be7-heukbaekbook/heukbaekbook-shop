package com.nhnacademy.heukbaekbookshop.memberset.member.dto.response;

import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderDetailResponse;

public record MyPageOrderDetailResponse(
        MemberResponse memberResponse,
        OrderDetailResponse orderDetailResponse
) {
}
