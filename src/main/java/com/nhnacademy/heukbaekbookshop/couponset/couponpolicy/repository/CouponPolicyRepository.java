package com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.repository;

import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long> {
    Page<CouponPolicy> findCouponPolicyByDiscountTypeOrderByMinimumPurchaseAmount(DiscountType discountType, Pageable pageable);

    Page<CouponPolicy> findAllByOrderByDiscountTypeAscDiscountAmountAsc(Pageable pageable);

    List<CouponPolicy> findAllByOrderByDiscountTypeAscDiscountAmountAsc();

    CouponPolicy findCouponPolicyByDiscountTypeAndDiscountAmount(DiscountType discountType, BigDecimal discountAmount);
}

