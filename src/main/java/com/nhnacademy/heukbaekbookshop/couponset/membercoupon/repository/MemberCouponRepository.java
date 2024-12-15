package com.nhnacademy.heukbaekbookshop.couponset.membercoupon.repository;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.domain.MemberCoupon;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
    Page<MemberCoupon> findByMemberId(Pageable pageable, Long memberId);

    boolean existsByMemberAndCoupon(Member member, Coupon coupon);
}

