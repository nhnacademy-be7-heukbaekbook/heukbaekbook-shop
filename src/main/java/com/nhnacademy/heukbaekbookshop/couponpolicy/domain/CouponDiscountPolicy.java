package com.nhnacademy.heukbaekbookshop.couponpolicy.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "coupons_discount_policies")
public class CouponDiscountPolicy {

    @Id
    @Column(name = "coupon_discount_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_discount_type")
    private DisCountType disCountType;
}
