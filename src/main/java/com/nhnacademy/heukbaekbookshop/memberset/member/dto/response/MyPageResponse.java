package com.nhnacademy.heukbaekbookshop.memberset.member.dto.response;

import com.nhnacademy.heukbaekbookshop.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderResponse;

public record MyPageResponse(
        GradeDto gradeDto,
        OrderResponse orderResponse
) {
}
