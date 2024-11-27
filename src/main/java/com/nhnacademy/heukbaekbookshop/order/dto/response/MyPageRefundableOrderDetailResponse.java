package com.nhnacademy.heukbaekbookshop.order.dto.response;

import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MemberResponse;

import java.util.List;

public record MyPageRefundableOrderDetailResponse(
        MemberResponse memberResponse,
        List<RefundableOrderDetailResponse> orders
) {}
