package com.nhnacademy.heukbaekbookshop.point.history.service;

import com.nhnacademy.heukbaekbookshop.point.history.domain.PointHistory;
import com.nhnacademy.heukbaekbookshop.point.history.domain.PointType;
import com.nhnacademy.heukbaekbookshop.point.history.dto.request.PointHistoryRequest;
import com.nhnacademy.heukbaekbookshop.point.history.dto.response.PointHistoryResponse;

import java.util.List;

public interface PointSaveService {
    PointHistoryResponse createPointHistory(Long customerId, PointHistoryRequest pointHistoryRequest);

    List<PointHistory> getPointHistories(Long customerId, Long orderId, PointType type);
}
