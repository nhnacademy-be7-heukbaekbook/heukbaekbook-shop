package com.nhnacademy.heukbaekbookshop.couponpolicy.domain;

import jakarta.persistence.*;
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
public class CouponDiscountPercent extends CouponDiscountPolicy {

    @NotNull
    @Column(name = "discount_rate")
    private BigDecimal percent;

    @NotNull
    @Column(name = "minumum_purchase_amount")
    private BigDecimal minimumPurchaseAmount;

    @Column(name = "maximum_discount_amount")
    private BigDecimal maximumDiscountAmount;
}
