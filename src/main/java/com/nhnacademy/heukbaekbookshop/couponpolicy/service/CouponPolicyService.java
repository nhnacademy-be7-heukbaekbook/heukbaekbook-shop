package com.nhnacademy.heukbaekbookshop.couponpolicy.service;

import com.nhnacademy.heukbaekbookshop.couponpolicy.dto.PolicyRequest;
import com.nhnacademy.heukbaekbookshop.couponpolicy.dto.PolicyResponse;

import java.util.List;

public interface CouponPolicyService {
    PolicyResponse createPolicy(PolicyRequest policyRequest);
    PolicyResponse getPolicyById(long policyId);
    List<PolicyResponse> getAllPolicies();
    PolicyResponse updatePolicy(long policyId, PolicyRequest policyRequest);
    void deletePolicy(long policyId);
}
