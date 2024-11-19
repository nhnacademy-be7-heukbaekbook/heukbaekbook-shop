package com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.service;

import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyRequest;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponPolicyService {
    CouponPolicyResponse createCouponPolicy(CouponPolicyRequest policyRequest);
    CouponPolicyResponse getCouponPolicyById(long policyId);
    Page<CouponPolicyResponse> getAllCouponPolicies(Pageable pageable);
    Page<CouponPolicyResponse> getAllCouponPoliciesByType(DiscountType disCountType, Pageable pageable);
    CouponPolicyResponse updateCouponPolicy(long policyId, CouponPolicyRequest policyRequest);
    void deleteCouponPolicy(long policyId);
}
