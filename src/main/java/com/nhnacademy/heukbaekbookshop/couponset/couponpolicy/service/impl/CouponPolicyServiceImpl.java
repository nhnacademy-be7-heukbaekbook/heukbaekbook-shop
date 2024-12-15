package com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.service.impl;

import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyRequest;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyResponse;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.mapper.CouponPolicyMapper;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.exception.CouponPolicyNotFoundException;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.repository.CouponPolicyRepository;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.service.CouponPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class CouponPolicyServiceImpl implements CouponPolicyService {

    private final CouponPolicyRepository couponPolicyRepository;

    @Override
    @Transactional
    public CouponPolicyResponse createCouponPolicy(CouponPolicyRequest couponPolicyRequest) {
        CouponPolicy couponPolicy = couponPolicyRepository.save(CouponPolicyMapper.toEntity(couponPolicyRequest));
        return CouponPolicyMapper.fromEntity(couponPolicy);
    }


    @Override
    public CouponPolicyResponse getCouponPolicyById(Long policyId) {
        return CouponPolicyMapper.fromEntity(
                couponPolicyRepository.findById(policyId)
                        .orElseThrow(CouponPolicyNotFoundException::new)
        );
    }

    @Override
    public Page<CouponPolicyResponse> getAllCouponPolicies(Pageable pageable) {
        return CouponPolicyMapper.fromPageableEntity(
                couponPolicyRepository.findAllByOrderByDiscountTypeAscDiscountAmountAsc(pageable)
        );
    }

    @Override
    public List<CouponPolicyResponse> getAllCouponPolicyList() {
        return CouponPolicyMapper.fromEntityList(couponPolicyRepository.findAllByOrderByDiscountTypeAscDiscountAmountAsc());
    }

    @Override
    public Page<CouponPolicyResponse> getAllCouponPoliciesByType(DiscountType discountType, Pageable pageable) {
        return CouponPolicyMapper.fromPageableEntity(
                couponPolicyRepository.findCouponPolicyByDiscountTypeOrderByMinimumPurchaseAmount(discountType, pageable)
        );
    }

    @Override
    @Transactional
    public CouponPolicyResponse updateCouponPolicy(Long policyId, CouponPolicyRequest couponPolicyRequest) {
        CouponPolicy couponPolicy = couponPolicyRepository.findById(policyId)
                .orElseThrow(CouponPolicyNotFoundException::new);

        return CouponPolicyMapper.fromEntity(
                couponPolicy.modifyCouponPolicy(couponPolicyRequest)
        );
    }

    @Override
    @Transactional
    public void deleteCouponPolicy(Long policyId) {
        if(!couponPolicyRepository.existsById(policyId)) {
            throw new CouponPolicyNotFoundException();
        }

        couponPolicyRepository.deleteById(policyId);
    }

    @Override
    public CouponPolicyResponse getCouponPolicyByDiscountTypeAndDiscountAmount(DiscountType discountType, BigDecimal discountAmount) {
        CouponPolicy couponPolicy = couponPolicyRepository.findCouponPolicyByDiscountTypeAndDiscountAmount(discountType, discountAmount);

        return Objects.isNull(couponPolicy) ? null : CouponPolicyMapper.fromEntity(couponPolicy);
    }

}

