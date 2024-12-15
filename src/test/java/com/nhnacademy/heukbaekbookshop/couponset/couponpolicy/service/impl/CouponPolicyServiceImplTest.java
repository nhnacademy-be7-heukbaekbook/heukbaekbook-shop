package com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.service.impl;

import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyRequest;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyResponse;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.repository.CouponPolicyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponPolicyServiceImplTest {

    @InjectMocks
    private CouponPolicyServiceImpl couponPolicyService;

    @Mock
    private CouponPolicyRepository couponPolicyRepository;

    @Test
    void createCouponPolicy() {
        //given
        CouponPolicyRequest couponPolicyRequest = new CouponPolicyRequest(
                DiscountType.FIXED,
                BigDecimal.valueOf(3000),
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(50000)
        );

        CouponPolicy couponPolicy = CouponPolicy.builder()
                .discountType(couponPolicyRequest.discountType())
                .discountAmount(couponPolicyRequest.discountAmount())
                .minimumPurchaseAmount(couponPolicyRequest.minimumPurchaseAmount())
                .maximumPurchaseAmount(couponPolicyRequest.maximumPurchaseAmount())
                .build();

        when(couponPolicyRepository.save(any(CouponPolicy.class))).thenReturn(couponPolicy);

        //when
        couponPolicyService.createCouponPolicy(couponPolicyRequest);

        //then
        verify(couponPolicyRepository, times(1)).save(any(CouponPolicy.class));
    }

    @Test
    void getCouponPolicyById() {
        //given
        Long policyId = 1L;

        CouponPolicy couponPolicy = CouponPolicy.builder()
                .discountType(DiscountType.FIXED)
                .discountAmount(BigDecimal.valueOf(3000))
                .minimumPurchaseAmount(BigDecimal.valueOf(10000))
                .maximumPurchaseAmount(BigDecimal.valueOf(50000))
                .build();

        when(couponPolicyRepository.findById(policyId)).thenReturn(Optional.of(couponPolicy));

        //when
        CouponPolicyResponse couponPolicyResponse = couponPolicyService.getCouponPolicyById(policyId);

        //then
        assertThat(couponPolicyResponse).isNotNull();
        assertThat(couponPolicyResponse.discountAmount()).isEqualTo(couponPolicy.getDiscountAmount());

    }

    @Test
    void getAllCouponPolicies() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);
        CouponPolicy couponPolicy = CouponPolicy.builder()
                .discountType(DiscountType.FIXED)
                .discountAmount(BigDecimal.valueOf(3000))
                .minimumPurchaseAmount(BigDecimal.valueOf(10000))
                .maximumPurchaseAmount(BigDecimal.valueOf(50000))
                .build();

        List<CouponPolicy> couponPolicies = List.of(couponPolicy);

        when(couponPolicyRepository.findAllByOrderByDiscountTypeAscDiscountAmountAsc(pageRequest)).thenReturn(new PageImpl<>(couponPolicies));

        //when
        Page<CouponPolicyResponse> result = couponPolicyService.getAllCouponPolicies(pageRequest);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(couponPolicies.size());
    }

    @Test
    void getAllCouponPolicyList() {
    }

    @Test
    void getAllCouponPoliciesByType() {
    }

    @Test
    void updateCouponPolicy() {
    }

    @Test
    void deleteCouponPolicy() {
    }

    @Test
    void getCouponPolicyByDiscountTypeAndDiscountAmount() {
    }
}