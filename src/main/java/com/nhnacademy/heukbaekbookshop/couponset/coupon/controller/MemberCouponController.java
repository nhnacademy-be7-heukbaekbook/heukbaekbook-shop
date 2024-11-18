package com.nhnacademy.heukbaekbookshop.couponset.coupon.controller;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponHistoryResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.MemberCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/coupons")
public class MemberCouponController {
    private final CouponService couponService;

    @GetMapping("/{memberId}")
    public ResponseEntity<Page<MemberCouponResponse>> getUserCoupons(Pageable pageable, Long memberId) {
        return ResponseEntity.ok(couponService.getUserCoupons(pageable, memberId));
    }

    @GetMapping("/{memberCouponId}")
    public ResponseEntity<Page<CouponHistoryResponse>> getCouponUsageHistory(Pageable pageable, Long memberCouponId) {
        return ResponseEntity.ok(couponService.getCouponHistory(pageable, memberCouponId));
    }

}
