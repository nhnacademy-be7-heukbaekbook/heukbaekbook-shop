package com.nhnacademy.heukbaekbookshop.point.history.controller;

import com.nhnacademy.heukbaekbookshop.point.history.dto.response.PointHistoryResponse;
import com.nhnacademy.heukbaekbookshop.point.history.service.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PointHistoryController {

    private final PointHistoryService pointHistoryService;

    @GetMapping("/api/members/{memberId}/points/histories")
    public Page<PointHistoryResponse> getPointHistoriesByMemberId(
            @PathVariable Long memberId,
            Pageable pageable
    ) {
        return pointHistoryService.getPointHistoriesByCustomerId(memberId, pageable);
    }
}
