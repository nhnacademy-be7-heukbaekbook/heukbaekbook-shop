package com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto;

import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DisCountType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CouponPolicyRequest(
   @NotNull
   DisCountType discountType,
   @NotNull
   BigDecimal discountAmount,
   @NotNull
   BigDecimal minimumPurchaseAmount,

   BigDecimal maximumPurchaseAmount
) {
}
