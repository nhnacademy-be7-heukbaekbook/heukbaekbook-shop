package com.nhnacademy.heukbaekbookshop.couponset.coupon.controller;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class DownloadCouponController {
    private final CouponService couponService;


    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<CouponResponse>> getDownloadableCouponsByBookId(@PathVariable Long bookId) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(couponService.getDownloadableCoupons(bookId));
    }
}
