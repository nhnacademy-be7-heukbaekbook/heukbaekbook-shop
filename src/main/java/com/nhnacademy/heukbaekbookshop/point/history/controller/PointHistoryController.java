package com.nhnacademy.heukbaekbookshop.point.history.controller;

import com.nhnacademy.heukbaekbookshop.point.history.dto.response.PointHistoryResponse;
import com.nhnacademy.heukbaekbookshop.point.history.service.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/points")
public class PointHistoryController {

    private final PointHistoryService pointHistoryService;

    @GetMapping("/histories")
    public ResponseEntity<Page<PointHistoryResponse>> getPointHistoriesByMemberId(
            @RequestHeader("X-USER-ID") Long memberId,
            Pageable pageable
    ) {
        Page<PointHistoryResponse> pointHistoriesByCustomerId = pointHistoryService.getPointHistoriesByCustomerId(memberId, pageable);
        return ResponseEntity.ok(pointHistoriesByCustomerId);
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getCurrentBalanceByMemberId(@RequestHeader("X-USER-ID") Long memberId) {
        BigDecimal currentBalanceByCustomerId = pointHistoryService.getCurrentBalanceByCustomerId(memberId);
        return ResponseEntity.ok(currentBalanceByCustomerId);
    }
}
