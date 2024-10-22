package com.nhnacademy.heukbaekbookshop.coupon.domain;

import com.nhnacademy.heukbaekbookshop.member.domain.Member;
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
@Table(name = "members_coupons")
public class MemberCoupon {

    @Id
    @Column(name = "member_coupon_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @NotNull
    @Column(name = "is_coupon_used")
    private boolean isCouponUsed;

    @NotNull
    @Column(name = "coupon_created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "coupon_expiration_at")
    private LocalDateTime expirationAt;

}
