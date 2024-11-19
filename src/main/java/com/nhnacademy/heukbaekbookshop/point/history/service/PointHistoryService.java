package com.nhnacademy.heukbaekbookshop.point.history.service;

import com.nhnacademy.heukbaekbookshop.point.history.dto.response.PointHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface PointHistoryService {
    Page<PointHistoryResponse> getPointHistoriesByCustomerId(Long customerId, Pageable pageable);

    BigDecimal getCurrentBalanceByCustomerId(Long customerId);
}
