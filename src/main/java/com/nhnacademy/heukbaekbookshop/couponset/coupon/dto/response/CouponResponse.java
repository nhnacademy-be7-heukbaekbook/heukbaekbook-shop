package com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response;

import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyResponse;

import java.time.LocalDateTime;

public record CouponResponse(
        Long couponId,
        CouponPolicyResponse couponPolicyResponse,
        String couponStatus,
        int availableDuration,
        LocalDateTime couponTimeStart,
        LocalDateTime couponTimeEnd,
        String couponName,
        String couponDescription,
        LocalDateTime couponCreatedAt
) {
}