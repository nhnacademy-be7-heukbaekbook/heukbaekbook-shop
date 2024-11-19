package com.nhnacademy.heukbaekbookshop.point.history.controller;

import com.nhnacademy.heukbaekbookshop.point.history.dto.request.PointHistoryRequest;
import com.nhnacademy.heukbaekbookshop.point.history.dto.response.PointHistoryResponse;
import com.nhnacademy.heukbaekbookshop.point.history.service.PointHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class PointHistoryController {

    private final PointHistoryService pointHistoryService;

    @GetMapping("/api/points/histories")
    public ResponseEntity<Page<PointHistoryResponse>> getPointHistoriesByMemberId(
            @RequestHeader("X-USER-ID") Long memberId,
            Pageable pageable
    ) {
        Page<PointHistoryResponse> pointHistoriesByCustomerId = pointHistoryService.getPointHistoriesByCustomerId(memberId, pageable);
        return ResponseEntity.ok(pointHistoriesByCustomerId);
    }

    @GetMapping("/api/points/balance")
    public ResponseEntity<BigDecimal> getCurrentBalanceByMemberId(@RequestHeader("X-USER-ID") Long memberId) {
        BigDecimal currentBalanceByCustomerId = pointHistoryService.getCurrentBalanceByCustomerId(memberId);
        return ResponseEntity.ok(currentBalanceByCustomerId);
    }

    @PostMapping("/api/points/histories")
    public ResponseEntity<PointHistoryResponse> createPointHistory(
            @RequestHeader("X-USER-ID") Long memberId,
            @Valid @RequestBody PointHistoryRequest request
    ) {
        PointHistoryResponse pointHistory = pointHistoryService.createPointHistory(memberId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(pointHistory);
    }
}
