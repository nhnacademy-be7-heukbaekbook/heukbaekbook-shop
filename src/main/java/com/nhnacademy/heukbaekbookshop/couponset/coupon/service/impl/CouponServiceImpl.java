package com.nhnacademy.heukbaekbookshop.couponset.coupon.service.impl;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.CouponStatus;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.mapper.CouponMapper;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.request.CouponRequest;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.exception.CouponNotFoundException;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.service.CouponService;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DisCountType;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.exception.CouponPolicyNotFoundException;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.repository.CouponPolicyRepository;
import lombok.RequiredArgsConstructor;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.CouponRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponPolicyRepository couponPolicyRepository;

    @Override
    public CouponResponse createCoupon(CouponRequest couponRequest) {
        CouponPolicy couponPolicy = couponPolicyRepository.findById(couponRequest.policyId())
                .orElseThrow(CouponPolicyNotFoundException::new);

        return CouponMapper.fromEntity(
                couponRepository.save(
                        CouponMapper.toEntity(couponRequest, couponPolicy))
        );
    }

    @Override
    public CouponResponse getCoupon(Long couponId) {
        return CouponMapper.fromEntity(
                couponRepository.findById(couponId)
                        .orElseThrow(CouponNotFoundException::new)
        );
    }

    @Override
    public Page<CouponResponse> getAllCoupons(Pageable pageable) {
        Page<Coupon> coupons = couponRepository.findAllByPageable(pageable);
        return CouponMapper.fromPageableEntity(coupons, pageable);
    }

    @Override
    public Page<CouponResponse> getCouponsByType(DisCountType discountType, Pageable pageable) {
        Page<Coupon> coupons = couponRepository.findAllByDiscountType(discountType, pageable);
        return CouponMapper.fromPageableEntity(coupons, pageable);
    }

    @Override
    public Page<CouponResponse> getCouponsByStatus(CouponStatus couponStatus, Pageable pageable) {
        Page<Coupon> coupons = couponRepository.findAllByCouponStatus(couponStatus, pageable);
        return CouponMapper.fromPageableEntity(coupons, pageable);
    }

    @Override
    public CouponResponse updateCoupon(Long couponId, CouponRequest couponRequest) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(CouponNotFoundException::new);
        CouponPolicy couponPolicy = couponPolicyRepository.findById(couponRequest.policyId()).orElseThrow(CouponPolicyNotFoundException::new);

        return CouponMapper.fromEntity(
                coupon.modifyCoupon(
                        couponPolicy,
                        couponRequest.availableDuration(),
                        couponRequest.couponTimeStart(),
                        couponRequest.couponTimeEnd(),
                        couponRequest.couponName(),
                        couponRequest.couponDescription()
                )
        );
    }

    @Override
    public void deleteCoupon(Long couponId) {
        couponRepository.delete(
                couponRepository.findById(couponId)
                        .orElseThrow(CouponNotFoundException::new)
        );
    }
}
