package com.nhnacademy.heukbaekbookshop.couponset.coupon.repository;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.CouponStatus;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DisCountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponRepositoryCustom {
    Page<Coupon> findAllByPageable(Pageable pageable);

    Page<Coupon> findAllByCouponStatus(CouponStatus couponStatus, Pageable pageable);

    Page<Coupon> findAllByDiscountType(DisCountType disCountType, Pageable pageable);

}
