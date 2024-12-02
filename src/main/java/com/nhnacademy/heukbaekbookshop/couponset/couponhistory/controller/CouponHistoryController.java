package com.nhnacademy.heukbaekbookshop.couponset.couponhistory.controller;

import com.nhnacademy.heukbaekbookshop.couponset.couponhistory.dto.request.CouponHistoryRequest;
import com.nhnacademy.heukbaekbookshop.couponset.couponhistory.dto.response.CouponHistoryResponse;
import com.nhnacademy.heukbaekbookshop.couponset.couponhistory.service.CouponHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/coupons/histories")
public class CouponHistoryController {
    public static final String X_USER_ID = "X-USER-ID";

    private final CouponHistoryService couponHistoryService;

    /**
     * 사용자 쿠폰 사용 내역 조회
     *
     * @param customerId 회원 ID
     * @param pageable 페이징 정보
     * @return 쿠폰 사용 내역 목록
     */
    @GetMapping()
    public ResponseEntity<Page<CouponHistoryResponse>> getCouponHistoriesByUser(
            @RequestHeader(X_USER_ID) Long customerId,
            Pageable pageable
    ) {
        Page<CouponHistoryResponse> histories = couponHistoryService.getCouponHistoryByCustomerId(customerId, pageable);
        return ResponseEntity.ok(histories);
    }

    /**
     * 쿠폰 사용 기록 생성
     *
     * @param couponHistoryRequest 쿠폰 사용 요청 DTO
     * @return 성공 여부
     */
    @PostMapping
    public ResponseEntity<Void> createCouponHistory(@RequestBody CouponHistoryRequest couponHistoryRequest) {
        couponHistoryService.createCouponHistory(couponHistoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
