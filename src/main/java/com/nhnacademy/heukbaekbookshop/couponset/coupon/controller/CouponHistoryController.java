package com.nhnacademy.heukbaekbookshop.couponset.coupon.controller;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponHistoryResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.service.CouponHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons/histories")
@RequiredArgsConstructor
public class CouponHistoryController {
    private final CouponHistoryService couponHistoryService;

    /**
     * 사용자 쿠폰 사용 내역 조회
     * @param memberId 사용자 ID
     * @param pageable 페이징 객체
     * @return 쿠폰 사용 내역
     */
    @GetMapping("/members/{memberId}")
    public ResponseEntity<Page<CouponHistoryResponse>> getCouponHistoriesByUser(
            @PathVariable Long memberId,
            Pageable pageable) {
        Page<CouponHistoryResponse> responses = couponHistoryService.getCouponHistoryByCustomerId(memberId, pageable);
        return ResponseEntity.ok(responses);
    }
}

