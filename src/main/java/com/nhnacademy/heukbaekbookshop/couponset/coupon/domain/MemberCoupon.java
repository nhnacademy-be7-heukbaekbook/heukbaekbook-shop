package com.nhnacademy.heukbaekbookshop.couponset.coupon.domain;

import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
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
    @Column(name = "coupon_created_at")
    private LocalDateTime issuedAt;

    @NotNull
    @Column(name = "coupon_expiration_date")
    private LocalDateTime expirationAt;


    @Builder
    private MemberCoupon(Member member, Coupon coupon, LocalDateTime issuedAt, LocalDateTime expirationAt) {
        this.member = member;
        this.coupon = coupon;
        this.issuedAt = issuedAt;
        this.expirationAt = expirationAt;
        this.isCouponUsed = false;
    }

    public void markAsUsed() {
        if (this.isCouponUsed) {
            throw new IllegalStateException("쿠폰이 이미 사용되었습니다");
        }
        this.isCouponUsed = true;
    }

}
