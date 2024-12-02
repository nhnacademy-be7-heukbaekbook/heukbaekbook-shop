package com.nhnacademy.heukbaekbookshop.point.history.service;

import com.nhnacademy.heukbaekbookshop.point.history.dto.request.PointHistoryRequest;
import com.nhnacademy.heukbaekbookshop.point.history.dto.response.PointHistoryResponse;

public interface PointSaveService {
    PointHistoryResponse createPointHistory(Long customerId, PointHistoryRequest pointHistoryRequest);
}
