package com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.repository;

import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long> {
    Page<CouponPolicy> findCouponPolicyByDiscountTypeOrderByMinimumPurchaseAmount(DiscountType discountType, Pageable pageable);
}