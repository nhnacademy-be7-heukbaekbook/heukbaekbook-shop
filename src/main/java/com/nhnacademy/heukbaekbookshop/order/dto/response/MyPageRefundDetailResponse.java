package com.nhnacademy.heukbaekbookshop.order.dto.response;

import com.nhnacademy.heukbaekbookshop.memberset.grade.dto.GradeDto;

import java.util.List;

public record MyPageRefundDetailResponse(
        GradeDto gradeDto,
        List<RefundDetailResponse> refunds
) {}
