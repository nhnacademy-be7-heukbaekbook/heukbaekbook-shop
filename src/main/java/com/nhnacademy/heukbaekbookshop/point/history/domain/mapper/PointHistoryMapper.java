package com.nhnacademy.heukbaekbookshop.point.history.domain.mapper;

import com.nhnacademy.heukbaekbookshop.point.history.domain.PointHistory;
import com.nhnacademy.heukbaekbookshop.point.history.dto.response.PointHistoryResponse;

public class PointHistoryMapper {

    private PointHistoryMapper() {
    }

    public static PointHistoryResponse toResponse(PointHistory pointHistory) {
        return new PointHistoryResponse(
                pointHistory.getId(),
                pointHistory.getMember().getId(),
                pointHistory.getOrder() != null ? pointHistory.getOrder().getId() : null,
                pointHistory.getAmount(),
                pointHistory.getCreatedAt(),
                pointHistory.getBalance(),
                pointHistory.getType()
        );
    }
}
