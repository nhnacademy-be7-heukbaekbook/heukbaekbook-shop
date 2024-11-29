package com.nhnacademy.heukbaekbookshop.order.dto.response;

import com.nhnacademy.heukbaekbookshop.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MemberResponse;

import java.util.List;

public record MyPageRefundableOrderDetailResponse(
        GradeDto gradeDto,
        List<RefundableOrderDetailResponse> orders
) {}
