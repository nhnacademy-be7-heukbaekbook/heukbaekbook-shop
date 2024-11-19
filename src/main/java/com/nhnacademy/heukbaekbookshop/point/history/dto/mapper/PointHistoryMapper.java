package com.nhnacademy.heukbaekbookshop.point.history.dto.mapper;

import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.point.history.domain.PointHistory;
import com.nhnacademy.heukbaekbookshop.point.history.dto.request.PointHistoryRequest;
import com.nhnacademy.heukbaekbookshop.point.history.dto.response.PointHistoryResponse;

import java.math.BigDecimal;

public class PointHistoryMapper {

    private PointHistoryMapper() {
    }

    public static PointHistory toEntity(PointHistoryRequest request, Member member, BigDecimal newBalance) {
        PointHistory pointHistory = new PointHistory();
        pointHistory.setMember(member);
        pointHistory.setAmount(request.amount());
        pointHistory.setCreatedAt(request.createdAt());
        pointHistory.setBalance(newBalance);
        pointHistory.setType(request.type());
        return pointHistory;
    }

    public static PointHistoryResponse toResponse(PointHistory pointHistory) {
        return new PointHistoryResponse(
                pointHistory.getId(),
                pointHistory.getMember().getId(),
                pointHistory.getOrder().getId() == null ? null : pointHistory.getOrder().getId(),
                pointHistory.getAmount(),
                pointHistory.getCreatedAt(),
                pointHistory.getBalance(),
                pointHistory.getType()
        );
    }
}
