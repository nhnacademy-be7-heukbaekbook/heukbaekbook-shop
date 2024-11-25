package com.nhnacademy.heukbaekbookshop.couponset.coupon.domain;

import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "coupons")
public class Coupon {

    @Id
    @Column(name = "coupon_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_policy_id")
    private CouponPolicy couponPolicy;

    @Column(name = "coupon_quantity")
    private int couponQuantity;

    @NotNull
    @Setter
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

    @NotNull
    @Column(name = "coupon_created_at")
    private LocalDateTime couponCreatedAt;

    @Builder
    public Coupon(CouponPolicy couponPolicy, int couponQuantity, int availableDuration, LocalDateTime couponTimeStart, LocalDateTime couponTimeEnd, String couponName, String couponDescription) {
        this.couponPolicy = couponPolicy;
        this.couponQuantity = couponQuantity;
        this.couponStatus = CouponStatus.PENDING;
        this.availableDuration = availableDuration;
        this.couponTimeStart = couponTimeStart;
        this.couponTimeEnd = couponTimeEnd;
        this.couponName = couponName;
        this.couponDescription = couponDescription;
        this.couponCreatedAt = LocalDateTime.now();
    }

    public Coupon modifyCoupon(CouponPolicy couponPolicy, int couponQuantity, int availableDuration, LocalDateTime couponTimeStart, LocalDateTime couponTimeEnd, String couponName, String couponDescription) {
        this.couponPolicy = couponPolicy;
        this.couponQuantity = couponQuantity;
        this.availableDuration = availableDuration;
        this.couponTimeStart = couponTimeStart;
        this.couponTimeEnd = couponTimeEnd;
        this.couponName = couponName;
        this.couponDescription = couponDescription;
        return this;
    }

}
