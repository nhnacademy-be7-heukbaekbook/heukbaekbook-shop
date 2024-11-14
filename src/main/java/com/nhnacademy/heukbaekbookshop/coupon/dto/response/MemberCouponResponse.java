package com.nhnacademy.heukbaekbookshop.coupon.dto.response;

import com.nhnacademy.heukbaekbookshop.coupon.domain.MemberCoupon;

import java.time.LocalDateTime;

public record MemberCouponResponse(
        Long memberCouponId,
        Long couponId,
        LocalDateTime couponCreatedAt,
        LocalDateTime couponExpirationDate,
        Boolean isCouponUsed
) {
    public static MemberCouponResponse from(MemberCoupon memberCoupon) {
        return new MemberCouponResponse(
                memberCoupon.getId(),
                memberCoupon.getCoupon().getId(),
                memberCoupon.getCreatedAt(),
                memberCoupon.getExpirationAt(),
                memberCoupon.isCouponUsed()
        );
    }
}
