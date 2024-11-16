package com.nhnacademy.heukbaekbookshop.coupon.repository;

import com.nhnacademy.heukbaekbookshop.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.coupon.domain.CouponType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
//    List<Coupon> findByCouponTypeId(Long couponTypeId);
    Optional<Coupon> findById(Long id);
}
