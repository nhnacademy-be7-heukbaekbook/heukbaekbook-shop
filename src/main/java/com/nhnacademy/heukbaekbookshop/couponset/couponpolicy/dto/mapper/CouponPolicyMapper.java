package com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.mapper;

import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyRequest;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyResponse;
import org.springframework.data.domain.Page;

import java.util.List;

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

    public static List<CouponPolicyResponse> fromEntityList(List<CouponPolicy> couponPolicyList) {
        return couponPolicyList.stream().map(CouponPolicyMapper::fromEntity).toList();
    }

    public static CouponPolicy toEntity(CouponPolicyRequest couponPolicyRequest) {
        return CouponPolicy.builder()
                .discountType(couponPolicyRequest.discountType())
                .discountAmount(couponPolicyRequest.discountAmount())
                .minimumPurchaseAmount(couponPolicyRequest.minimumPurchaseAmount())
                .maximumPurchaseAmount(couponPolicyRequest.maximumPurchaseAmount())
                .build();
    }
}
