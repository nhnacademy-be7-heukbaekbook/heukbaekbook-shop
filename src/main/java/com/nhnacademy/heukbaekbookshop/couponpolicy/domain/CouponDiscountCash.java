package com.nhnacademy.heukbaekbookshop.couponpolicy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponDiscountCash extends CouponDiscountPolicy{

    @NotNull
    @Column(name = "discount_value")
    private BigDecimal discountValue;

    @NotNull
    @Column(name = "minumum_purchase_amount")
    private BigDecimal minimumPurchaseAmount;
}

