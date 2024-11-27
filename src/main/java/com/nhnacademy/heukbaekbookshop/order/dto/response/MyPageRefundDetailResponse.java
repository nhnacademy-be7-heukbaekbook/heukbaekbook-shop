package com.nhnacademy.heukbaekbookshop.order.dto.response;

import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MemberResponse;

import java.util.List;

public record MyPageRefundDetailResponse(
        MemberResponse memberResponse,
        List<RefundDetailResponse> refunds
) {}
