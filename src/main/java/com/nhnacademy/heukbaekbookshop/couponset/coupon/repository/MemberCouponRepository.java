package com.nhnacademy.heukbaekbookshop.couponset.coupon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
    Page<MemberCoupon> findByMemberId(Pageable pageable, Long memberId);
}

