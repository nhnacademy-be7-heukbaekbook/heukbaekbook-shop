package com.nhnacademy.heukbaekbookshop.coupon.domain;

import com.nhnacademy.heukbaekbookshop.couponpolicy.domain.Policy;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "policy_id")
    private Policy policy;

    @OneToOne(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private BookCoupon bookCoupon;

    @OneToOne(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private CategoryCoupon categoryCoupon;

    @ManyToOne
    @JoinColumn(name = "coupon_type_id")
    private CouponType couponType;

    @Column(name = "coupon_available_duration")
    private int availableDuration;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

}
