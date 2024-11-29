package com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.response;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponType;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyResponse;

import java.time.LocalDateTime;

public record UserBookCouponResponse(
        Long bookCouponId,
        CouponPolicyResponse couponPolicy,
        CouponType couponType,
        LocalDateTime couponTimeStart,
        LocalDateTime couponTimeEnd
        ) {
}
