package com.nhnacademy.heukbaekbookshop.coupon.controller;

import com.nhnacademy.heukbaekbookshop.coupon.dto.response.CouponHistoryResponse;
import com.nhnacademy.heukbaekbookshop.coupon.dto.response.CouponInfoResponse;
import com.nhnacademy.heukbaekbookshop.coupon.dto.response.MemberCouponResponse;
import com.nhnacademy.heukbaekbookshop.coupon.service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {
    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

//    @GetMapping("/{couponId}")
//    public ResponseEntity<CouponInfoResponse> getCouponDetails(Long couponId) {
//        return ResponseEntity.ok(couponService.getCouponDetails(couponId));
//    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<Page<MemberCouponResponse>> getUserCoupons(Pageable pageable,Long memberId) {
        return ResponseEntity.ok(couponService.getUserCoupons(pageable, memberId));
    }

    @GetMapping("/usage/{memberCouponId}")
    public ResponseEntity<Page<CouponHistoryResponse>> getCouponUsageHistory(Pageable pageable, Long memberCouponId) {
        return ResponseEntity.ok(couponService.getCouponHistory(pageable, memberCouponId));
    }
}
