package com.nhnacademy.heukbaekbookshop.couponset.membercoupon.domain;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @Column(name = "coupon_issued_at")
    private LocalDateTime couponIssuedAt;

    @NotNull
    @Column(name = "coupon_expiration_at")
    private LocalDateTime couponExpirationAt;


    @Builder
    private MemberCoupon(Member member, Coupon coupon, LocalDateTime issuedAt, LocalDateTime expirationAt) {
        this.member = member;
        this.coupon = coupon;
        this.couponIssuedAt = issuedAt;
        this.couponExpirationAt = expirationAt;
        this.isCouponUsed = false;
    }

    public void markAsUsed() {
        if (this.isCouponUsed) {
            throw new IllegalStateException("쿠폰이 이미 사용되었습니다");
        }
        this.isCouponUsed = true;
    }

}
