package com.nhnacademy.heukbaekbookshop.point.history.service;

import com.nhnacademy.heukbaekbookshop.point.history.dto.response.PointHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointHistoryService {
    Page<PointHistoryResponse> getPointHistoriesByCustomerId(Long customerId, Pageable pageable);
}
