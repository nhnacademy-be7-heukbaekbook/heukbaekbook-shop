package com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto;

import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DisCountType;

import java.math.BigDecimal;

public record CouponPolicyResponse(
        Long id,
        DisCountType discountType,
        BigDecimal discountAmount,
        BigDecimal minimumPurchaseAmount,
        BigDecimal maximumPurchaseAmount
) {
}