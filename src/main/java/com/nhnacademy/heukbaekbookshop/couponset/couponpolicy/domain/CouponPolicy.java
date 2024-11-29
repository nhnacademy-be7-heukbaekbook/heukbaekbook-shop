package com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain;


import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "coupons_policies")
public class CouponPolicy {

    @Id
    @Column(name = "coupon_policy_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DiscountType discountType;

    @NotNull
    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @NotNull
    @Column(name = "minimum_purchase_amount")
    private BigDecimal minimumPurchaseAmount;

    @Column(name = "maximum_purchase_amount")
    private BigDecimal maximumPurchaseAmount;

    @Builder
    public CouponPolicy(DiscountType discountType, BigDecimal discountAmount, BigDecimal minimumPurchaseAmount, BigDecimal maximumPurchaseAmount) {
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.minimumPurchaseAmount = minimumPurchaseAmount;
        this.maximumPurchaseAmount = maximumPurchaseAmount;
    }

    public CouponPolicy modifyCouponPolicy(CouponPolicyRequest couponPolicyRequest) {
        this.discountType = couponPolicyRequest.discountType();
        this.discountAmount = couponPolicyRequest.discountAmount();
        this.minimumPurchaseAmount = couponPolicyRequest.minimumPurchaseAmount();
        this.maximumPurchaseAmount = couponPolicyRequest.maximumPurchaseAmount();
        return this;
    }
}
