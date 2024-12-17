package com.nhnacademy.heukbaekbookshop.couponset.couponhistory.repository;

import com.nhnacademy.heukbaekbookshop.couponset.couponhistory.domain.CouponHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponHistoryRepository extends JpaRepository<CouponHistory, Long> {
    // 사용자 ID를 기준으로 쿠폰 사용 내역 조회
    Page<CouponHistory> findAllByMemberCoupon(Pageable pageable, Long memberId);
}


