package com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.response;

import java.time.LocalDateTime;

public record MemberCouponResponse(
        Long memberCouponId,
        Long couponId,
        String couponName,
        String couponDescription,
        Boolean isCouponUsed,
        LocalDateTime couponIssuedAt,
        LocalDateTime couponExpirationDate

) {
}
