package com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.service;

import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyRequest;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CouponPolicyService {
    CouponPolicyResponse createCouponPolicy(CouponPolicyRequest policyRequest);
    CouponPolicyResponse getCouponPolicyById(long policyId);
    Page<CouponPolicyResponse> getAllCouponPolicies(Pageable pageable);
    List<CouponPolicyResponse> getAllCouponPolicyList();
    Page<CouponPolicyResponse> getAllCouponPoliciesByType(DiscountType discountType, Pageable pageable);
    CouponPolicyResponse updateCouponPolicy(long policyId, CouponPolicyRequest policyRequest);
    void deleteCouponPolicy(long policyId);
}
