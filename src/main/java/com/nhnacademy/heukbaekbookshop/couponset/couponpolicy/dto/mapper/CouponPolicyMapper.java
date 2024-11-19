package com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.mapper;

import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyRequest;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyResponse;
import org.springframework.data.domain.Page;

public class CouponPolicyMapper {

    public static CouponPolicyResponse fromEntity(CouponPolicy couponPolicy) {
        return new CouponPolicyResponse(
                couponPolicy.getId(),
                couponPolicy.getDiscountType(),
                couponPolicy.getDiscountAmount(),
                couponPolicy.getMinimumPurchaseAmount(),
                couponPolicy.getMaximumPurchaseAmount()
        );
    }

    public static Page<CouponPolicyResponse> fromPageableEntity(Page<CouponPolicy> couponPolicyPage) {
        return couponPolicyPage.map(CouponPolicyMapper::fromEntity);
    }

    public static CouponPolicy toEntity(CouponPolicyRequest couponPolicyRequest) {
        return CouponPolicy.builder()
                .disCountType(couponPolicyRequest.discountType())
                .discountAmount(couponPolicyRequest.discountAmount())
                .minimumPurchaseAmount(couponPolicyRequest.minimumPurchaseAmount())
                .maximumPurchaseAmount(couponPolicyRequest.maximumPurchaseAmount())
                .build();
    }
}
