package com.nhnacademy.heukbaekbookshop.couponset.coupon.controller;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.CouponStatus;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.request.CouponRequest;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.service.CouponService;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/coupons")
public class CouponController {
    private final CouponService couponService;

    @GetMapping("/{couponId}")
    public ResponseEntity<CouponResponse> getCoupon(@PathVariable Long couponId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponService.getCoupon(couponId)
                );
    }

    @GetMapping
    public ResponseEntity<Page<CouponResponse>> getAllCoupons(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponService.getAllCoupons(pageable)
                );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<CouponResponse>> getCouponsByStatus(@PathVariable("status") CouponStatus couponStatus, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponService.getCouponsByStatus(couponStatus, pageable)
                );
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<CouponResponse>> getCouponsByType(@PathVariable("type") DiscountType discountType, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponService.getCouponsByType(discountType,pageable)
                );
    }


    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(@Valid @RequestBody CouponRequest couponRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(couponService.createCoupon(couponRequest)
                );
    }

    @PutMapping("/{couponId}")
    public ResponseEntity<CouponResponse> updateCoupon(@PathVariable("couponId") Long couponId, @Valid @RequestBody CouponRequest couponRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponService.updateCoupon(couponId, couponRequest)
                );
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable("couponId") Long couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
