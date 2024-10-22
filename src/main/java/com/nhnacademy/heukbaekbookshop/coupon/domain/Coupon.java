package com.nhnacademy.heukbaekbookshop.coupon.domain;

import jakarta.persistence.*;
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
    private long id;

    @ManyToOne
    @JoinColumn(name = "policy_id")
    private Policy policy;

    @ManyToOne
    @JoinColumn(name = "coupon_type_id")
    private CouponType couponType;

    @Column(name = "coupon_available_duration")
    private int availableDuration;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

}
