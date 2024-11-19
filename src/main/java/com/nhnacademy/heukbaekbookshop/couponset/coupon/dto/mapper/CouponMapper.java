package com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.mapper;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.CouponStatus;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.request.CouponRequest;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.mapper.CouponPolicyMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class CouponMapper {

    public static CouponResponse fromEntity(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                CouponPolicyMapper.fromEntity(coupon.getCouponPolicy()),
                coupon.getCouponStatus().getValue(),
                coupon.getAvailableDuration(),
                coupon.getCouponTimeStart(),
                coupon.getCouponTimeEnd(),
                coupon.getCouponName(),
                coupon.getCouponDescription(),
                coupon.getCouponCreatedAt()
        );
    }

    public static Page<CouponResponse> fromPageableEntity(Page<Coupon> coupons, Pageable pageable) {
        return coupons.map(CouponMapper::fromEntity);
    }

    public static Coupon toEntity(CouponRequest couponRequest, CouponPolicy couponPolicy) {
        return Coupon.builder()
                .couponPolicy(couponPolicy)
                .availableDuration(couponRequest.availableDuration())
                .couponTimeStart(couponRequest.couponTimeStart())
                .couponTimeEnd(couponRequest.couponTimeEnd())
                .couponName(couponRequest.couponName())
                .couponDescription(couponRequest.couponDescription())
                .build();
    }

}
