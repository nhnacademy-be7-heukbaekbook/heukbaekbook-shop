package com.nhnacademy.heukbaekbookshop.couponset.coupon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.CouponHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponHistoryRepository extends JpaRepository<CouponHistory, Long> {
    Page<CouponHistory> findByMemberCouponId(Pageable pageable, Long memberCouponId);
}


