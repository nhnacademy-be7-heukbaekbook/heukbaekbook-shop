package com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "coupons_policies")
public class CouponPolicy {

    @Id
    @Column(name = "coupon_policy_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DisCountType disCountType;

    @NotNull
    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @NotNull
    @Column(name = "minimum_purchase_amount")
    private BigDecimal minimumPurchaseAmount;

    @NotNull
    @Column(name = "maximum_purchase_amount")
    private BigDecimal maximumPurchaseAmount;
}
