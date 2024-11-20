package com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.MemberCoupon;

import java.time.LocalDateTime;

public record MemberCouponResponse(
        Long memberCouponId,
        Long couponId,
        Boolean isCouponUsed,
        LocalDateTime couponIssuedAt,
        LocalDateTime couponExpirationDate

) {
}
