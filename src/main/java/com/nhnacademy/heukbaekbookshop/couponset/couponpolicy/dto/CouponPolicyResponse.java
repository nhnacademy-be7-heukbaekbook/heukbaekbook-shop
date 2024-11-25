package com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto;

import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public record CouponPolicyResponse(
        Long id,
        DiscountType discountType,
        BigDecimal discountAmount,
        BigDecimal minimumPurchaseAmount,
        BigDecimal maximumPurchaseAmount
)  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}