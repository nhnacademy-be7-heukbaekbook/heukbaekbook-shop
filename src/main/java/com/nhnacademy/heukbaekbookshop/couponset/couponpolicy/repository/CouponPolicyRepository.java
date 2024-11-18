package com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.repository;

import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long> {
}
