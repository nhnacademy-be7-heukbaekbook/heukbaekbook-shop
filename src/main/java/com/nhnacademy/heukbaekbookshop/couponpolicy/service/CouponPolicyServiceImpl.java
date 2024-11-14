package com.nhnacademy.heukbaekbookshop.couponpolicy.service;
import com.nhnacademy.heukbaekbookshop.couponpolicy.domain.Policy;
import com.nhnacademy.heukbaekbookshop.couponpolicy.dto.PolicyCreateRequest;
import com.nhnacademy.heukbaekbookshop.couponpolicy.dto.PolicyDeleteRequest;
import com.nhnacademy.heukbaekbookshop.couponpolicy.dto.PolicyResponse;
import com.nhnacademy.heukbaekbookshop.couponpolicy.dto.PolicyUpdateRequest;
import com.nhnacademy.heukbaekbookshop.couponpolicy.exception.PolicyNotFoundException;
import com.nhnacademy.heukbaekbookshop.couponpolicy.repository.PolicyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


@Service
public class CouponPolicyServiceImpl implements CouponPolicyService {
    private PolicyRepository policyRepository;

    public CouponPolicyServiceImpl(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Override
    @Transactional
    public PolicyResponse createPolicy(PolicyCreateRequest request){
        Policy policy = new Policy(
                request.discountType(),
                request.minimumPurchaseAmount(),
                request.maximumDiscountAmount(),
                request.discountValue()
        );
        Policy savedPolicy = policyRepository.save(policy);
        return new PolicyResponse(
                savedPolicy.getId(),
                savedPolicy.getDiscountType(),
                savedPolicy.getMinimumPurchaseAmount(),
                savedPolicy.getMaximumDiscountAmount(),
                savedPolicy.getDiscountValue()
        );
    }

    @Override
    @Transactional
    public PolicyResponse getPolicy(Long id){
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new PolicyNotFoundException("정책을 찾을 수 없습니다."));
        return new PolicyResponse(
                policy.getId(),
                policy.getDiscountType(),
                policy.getMinimumPurchaseAmount(),
                policy.getMaximumDiscountAmount(),
                policy.getDiscountValue()
        );
    }

    @Override
    @Transactional
    public PolicyResponse updatePolicy(Long id, PolicyUpdateRequest request) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new PolicyNotFoundException("정책을 찾을 수 없습니다."));

        policy.setDiscountType(request.disCountType());
        policy.setMinimumPurchaseAmount(request.minimumPurchaseAmount());
        policy.setMaximumDiscountAmount(request.maximumDiscountAmount());
        policy.setDiscountValue(request.discountValue());

        Policy updatedPolicy = policyRepository.save(policy);
        return new PolicyResponse(
                updatedPolicy.getId(),
                updatedPolicy.getDiscountType(),
                updatedPolicy.getMinimumPurchaseAmount(),
                updatedPolicy.getMaximumDiscountAmount(),
                updatedPolicy.getDiscountValue()
        );
    }

    @Override
    @Transactional
    public void deletePolicy(PolicyDeleteRequest request) {
        Policy policy = policyRepository.findById(request.id())
                .orElseThrow(() -> new PolicyNotFoundException("정책을 찾을 수 없습니다."));
        policyRepository.delete(policy);
    }
}





