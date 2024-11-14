package com.nhnacademy.heukbaekbookshop.couponpolicy.dto;

import com.nhnacademy.heukbaekbookshop.couponpolicy.domain.DisCountType;
import lombok.Getter;

import java.math.BigDecimal;

public record PolicyCreateRequest(
        DisCountType discountType,
        BigDecimal minimumPurchaseAmount,
        BigDecimal maximumDiscountAmount,
        BigDecimal discountValue
) {}
