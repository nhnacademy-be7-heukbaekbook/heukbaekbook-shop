package com.nhnacademy.heukbaekbookshop.order.dto.response;

import com.nhnacademy.heukbaekbookshop.memberset.grade.dto.GradeDto;

public record MyPageRefundableOrderDetailResponse(
        GradeDto gradeDto,
        RefundableOrderDetailResponse order
) {}
