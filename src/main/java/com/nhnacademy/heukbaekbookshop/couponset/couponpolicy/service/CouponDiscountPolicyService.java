package com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.service;

import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.PolicyRequest;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.PolicyResponse;

import java.util.List;

public interface CouponDiscountPolicyService {
    PolicyResponse createPolicy(PolicyRequest policyRequest);
    PolicyResponse getPolicyById(long policyId);
    List<PolicyResponse> getAllPolicies();
    PolicyResponse updatePolicy(long policyId, PolicyRequest policyRequest);
    void deletePolicy(long policyId);
}
