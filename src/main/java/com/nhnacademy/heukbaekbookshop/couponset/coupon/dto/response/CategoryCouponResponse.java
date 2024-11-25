package com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.CouponStatus;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CategoryCouponResponse (
        Long categoryCouponId,
        String couponName,
        String couponDescription,
        LocalDateTime couponCreatedAt,
        CouponStatus couponStatus,
        int couponQuantity,
        int availableDuration,
        LocalDateTime couponTimeStart,
        LocalDateTime couponTimeEnd,

        Long policyId,
        DiscountType discountType,
        BigDecimal discountAmount,
        BigDecimal minimumPurchaseAmount,
        BigDecimal maximumPurchaseAmount,

        Long categoryId,
        String name
        ){
}
