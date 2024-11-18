package com.nhnacademy.heukbaekbookshop.couponset.coupon.repository;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
//    List<Coupon> findByCouponTypeId(Long couponTypeId);
    Optional<Coupon> findById(Long id);
}
