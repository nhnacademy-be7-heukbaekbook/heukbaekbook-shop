package com.nhnacademy.heukbaekbookshop.couponset.coupon.repository;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CouponRepository extends JpaRepository<Coupon, Long>, CouponRepositoryCustom {

}
