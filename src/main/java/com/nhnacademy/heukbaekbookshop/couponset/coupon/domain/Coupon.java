package com.nhnacademy.heukbaekbookshop.couponset.coupon.domain;

import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupons")
public class Coupon {

    @Id
    @Column(name = "coupon_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "coupon_discount_id")
    private CouponPolicy couponDiscountPolicy;

    @Column(name = "coupon_amount")
    private int couponAmount;

    @Column(name = "coupon_status")
    @Enumerated(EnumType.STRING)
    private CouponStatus couponStatus;

    @NotNull
    @Column(name = "coupon_available_duration")
    private int availableDuration;

    @NotNull
    @Column(name = "coupon_time_start")
    private LocalDateTime couponTimeStart;

    @Column(name = "coupon_time_end")
    private LocalDateTime couponTimeEnd;

    @NotNull
    @Column(name = "coupon_name")
    private String couponName;

    @NotNull
    @Column(name = "coupon_description")
    private String couponDescription;

    @OneToOne(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private BookCoupon bookCoupon;

    @OneToOne(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private CategoryCoupon categoryCoupon;
}
