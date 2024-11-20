package com.nhnacademy.heukbaekbookshop.couponset.coupon.controller;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/coupons")
public class MemberCouponController {
    private final CouponService couponService;



}
