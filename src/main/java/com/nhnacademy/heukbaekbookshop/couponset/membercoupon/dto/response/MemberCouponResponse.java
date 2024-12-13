package com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.response;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponResponse;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MemberResponse;

import java.time.LocalDateTime;

public record MemberCouponResponse(
        CouponResponse couponResponse,
        MemberResponse memberResponse,
        Boolean isCouponUsed,
        LocalDateTime couponIssuedAt,
        LocalDateTime couponExpirationDate
) {
}
