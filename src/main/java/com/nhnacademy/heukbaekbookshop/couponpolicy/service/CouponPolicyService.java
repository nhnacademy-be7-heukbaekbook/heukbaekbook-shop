package com.nhnacademy.heukbaekbookshop.couponpolicy.service;

import com.nhnacademy.heukbaekbookshop.couponpolicy.dto.PolicyCreateRequest;
import com.nhnacademy.heukbaekbookshop.couponpolicy.dto.PolicyDeleteRequest;
import com.nhnacademy.heukbaekbookshop.couponpolicy.dto.PolicyResponse;
import com.nhnacademy.heukbaekbookshop.couponpolicy.dto.PolicyUpdateRequest;

public interface CouponPolicyService {
    PolicyResponse createPolicy(PolicyCreateRequest request);
    PolicyResponse getPolicy(Long id);
    PolicyResponse updatePolicy(Long id, PolicyUpdateRequest request);
    void deletePolicy(PolicyDeleteRequest request);

}
