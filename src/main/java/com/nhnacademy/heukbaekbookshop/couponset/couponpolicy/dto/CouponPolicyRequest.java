package com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto;

import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CouponPolicyRequest(
        @NotNull
        DiscountType discountType,

        @NotNull
        BigDecimal discountAmount,

        @NotNull
        BigDecimal minimumPurchaseAmount,

        BigDecimal maximumPurchaseAmount
) {
}
