//package com.nhnacademy.heukbaekbookshop.couponpolicy.service;
//
//import com.nhnacademy.heukbaekbookshop.couponpolicy.domain.DisCountType;
//import com.nhnacademy.heukbaekbookshop.couponpolicy.domain.Policy;
//import com.nhnacademy.heukbaekbookshop.couponpolicy.dto.PolicyRequest;
//import com.nhnacademy.heukbaekbookshop.couponpolicy.dto.PolicyResponse;
//import com.nhnacademy.heukbaekbookshop.couponpolicy.repository.PolicyRepository;
//import jakarta.transaction.Transactional;
//
//import java.util.List;
//
//public class CouponPolicyServiceImpl implements CouponPolicyService{
//    private final PolicyRepository policyRepository;
//
//    public CouponPolicyServiceImpl(PolicyRepository policyRepository) {
//        this.policyRepository = policyRepository;
//    }
//
//    @Override
//    @Transactional
//    public PolicyResponse createPolicy(PolicyRequest requestDto) {
//        Policy policy = new Policy();
//        policy.setDiscountType(DisCountType.valueOf(requestDto.getDiscountType()));
//        policy.setMinimumPurchaseAmount(requestDto.getMinimumPurchaseAmount());
//        policy.setMaximumDiscountAmount(requestDto.getMaximumDiscountAmount());
//        policy.setDiscountValue(requestDto.getDiscountValue());
//
//        Policy savedPolicy = policyRepository.save(policy);
//        return convertToDto(savedPolicy);
//    }
//
//
//    @Override
//    public PolicyResponse getPolicyById(long id) {
//        Policy policy = policyRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Policy not found"));
//        return convertToDto(policy);
//    }
//
//    @Override
//    public List<PolicyResponse> getAllPolicies() {
//        return policyRepository.findAll().stream()
//                .map(this::convertToDto)
//                .toList();
//    }
//
//    @Override
//    public PolicyResponse updatePolicy(long policyId, PolicyRequest policyRequest) {
//        return null;
//    }
//
//    @Override
//    public void deletePolicy(long policyId) {
//
//    }
//
//    @Override
//    @Transactional
//    public PolicyResponse updatePolicy(Long id, PolicyRequest requestDto) {
//        Policy existingPolicy = policyRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Policy not found"));
//        existingPolicy.setDiscountType(DisCountType.valueOf(requestDto.getDiscountType()));
//        existingPolicy.setMinimumPurchaseAmount(requestDto.getMinimumPurchaseAmount());
//        existingPolicy.setMaximumDiscountAmount(requestDto.getMaximumDiscountAmount());
//        existingPolicy.setDiscountValue(requestDto.getDiscountValue());
//
//        Policy updatedPolicy = policyRepository.save(existingPolicy);
//        return convertToDto(updatedPolicy);
//    }
//
//    private PolicyResponse convertToDto(Policy policy) {
//        PolicyResponse dto = new PolicyResponse();
//        dto.setId(policy.getId());
//        dto.setDiscountType(policy.getDiscountType().name());
//        dto.setMinimumPurchaseAmount(policy.getMinimumPurchaseAmount());
//        dto.setMaximumDiscountAmount(policy.getMaximumDiscountAmount());
//        dto.setDiscountValue(policy.getDiscountValue());
//        return dto;
//    }
//}
//
