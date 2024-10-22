package com.nhnacademy.heukbaekbookshop.coupon.domain;

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
@Table(name = "coupons_histories")
public class CouponHistory {
    @Id
    @Column(name = "coupon_history_id")
    private long id;

    @OneToOne
    @JoinColumn(name = "member_coupon_id")
    private MemberCoupon memberCoupon;

    @NotNull
    @Column(name = "coupon_used_at")
    private LocalDateTime usedAt;

    // TODO #1: 도서번호, 주문번호 외래 키 추가
}
