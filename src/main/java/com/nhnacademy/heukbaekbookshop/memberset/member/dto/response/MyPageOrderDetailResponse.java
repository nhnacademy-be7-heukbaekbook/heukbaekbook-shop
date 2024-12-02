package com.nhnacademy.heukbaekbookshop.memberset.member.dto.response;

import com.nhnacademy.heukbaekbookshop.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderDetailResponse;

public record MyPageOrderDetailResponse(
        GradeDto gradeDto,
        OrderDetailResponse orderDetailResponse
) {
}
