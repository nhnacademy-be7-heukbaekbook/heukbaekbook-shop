package com.nhnacademy.heukbaekbookshop.coupon;

import com.nhnacademy.heukbaekbookshop.couponpolicy.domain.DisCountType;
import com.nhnacademy.heukbaekbookshop.couponpolicy.domain.Policy;
import com.nhnacademy.heukbaekbookshop.couponpolicy.dto.PolicyCreateRequest;
import com.nhnacademy.heukbaekbookshop.couponpolicy.dto.PolicyResponse;
import com.nhnacademy.heukbaekbookshop.couponpolicy.exception.PolicyNotFoundException;
import com.nhnacademy.heukbaekbookshop.couponpolicy.repository.PolicyRepository;
import com.nhnacademy.heukbaekbookshop.couponpolicy.service.CouponPolicyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CouponPolicyServiceImplTest {

    @Mock
    private PolicyRepository policyRepository;

    @InjectMocks
    private CouponPolicyServiceImpl couponPolicyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePolicyWithFixedDiscount() {
        // Given
        PolicyCreateRequest request = new PolicyCreateRequest(
                DisCountType.FIXED,
                BigDecimal.valueOf(50000),
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(10000)
        );

        Policy policy = new Policy(
                request.discountType(),
                request.minimumPurchaseAmount(),
                request.maximumDiscountAmount(),
                request.discountValue()
        );

        when(policyRepository.save(any(Policy.class))).thenReturn(policy);

        // When
        PolicyResponse response = couponPolicyService.createPolicy(request);

        // Then
        assertEquals(DisCountType.FIXED, response.discountType());
        assertEquals(BigDecimal.valueOf(50000), response.minimumPurchaseAmount());
        assertEquals(BigDecimal.valueOf(10000), response.discountValue());
        verify(policyRepository, times(1)).save(any(Policy.class));
    }

    @Test
    void testCreatePolicyWithPercentageDiscount() {
        // Given
        PolicyCreateRequest request = new PolicyCreateRequest(
                DisCountType.PERCENTAGE,
                BigDecimal.valueOf(20000),
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(20)
        );

        Policy policy = new Policy(
                request.discountType(),
                request.minimumPurchaseAmount(),
                request.maximumDiscountAmount(),
                request.discountValue()
        );

        when(policyRepository.save(any(Policy.class))).thenReturn(policy);

        // When
        PolicyResponse response = couponPolicyService.createPolicy(request);

        // Then
        assertEquals(DisCountType.PERCENTAGE, response.discountType());
        assertEquals(BigDecimal.valueOf(20000), response.minimumPurchaseAmount());
        assertEquals(BigDecimal.valueOf(20), response.discountValue());
        verify(policyRepository, times(1)).save(any(Policy.class));
    }

    @Test
    void testApplyFixedDiscount() {
        // Given a fixed discount policy (e.g., 10,000 discount on a 50,000 purchase)
        Policy policy = new Policy(
                DisCountType.FIXED,
                BigDecimal.valueOf(50000),
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(10000)
        );
        BigDecimal purchaseAmount = BigDecimal.valueOf(60000);
        BigDecimal expectedDiscount = BigDecimal.valueOf(10000); // Fixed discount

        // When
        BigDecimal appliedDiscount = policy.getDiscountValue();

        // Then
        assertEquals(expectedDiscount, appliedDiscount);
    }

    @Test
    void testApplyPercentageDiscountWithMaxLimit() {
        // Given a percentage discount policy (20% discount on purchases over 20,000, max discount of 10,000)
        Policy policy = new Policy(
                DisCountType.PERCENTAGE,
                BigDecimal.valueOf(20000),
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(20)
        );
        BigDecimal purchaseAmount1 = BigDecimal.valueOf(30000);
        BigDecimal purchaseAmount2 = BigDecimal.valueOf(100000);

        BigDecimal expectedDiscount1 = BigDecimal.valueOf(6000).setScale(2); // Set scale for expected values
        BigDecimal expectedDiscount2 = BigDecimal.valueOf(10000).setScale(2);

        // When
        BigDecimal appliedDiscount1 = purchaseAmount1.multiply(policy.getDiscountValue().divide(BigDecimal.valueOf(100))).setScale(2);
        BigDecimal appliedDiscount2 = purchaseAmount2.multiply(policy.getDiscountValue().divide(BigDecimal.valueOf(100))).setScale(2);
        if (appliedDiscount2.compareTo(policy.getMaximumDiscountAmount()) > 0) {
            appliedDiscount2 = policy.getMaximumDiscountAmount().setScale(2);
        }

        // Then
        assertEquals(expectedDiscount1, appliedDiscount1);
        assertEquals(expectedDiscount2, appliedDiscount2);
    }


    @Test
    void testGetPolicyNotFound() {
        // Given
        Long policyId = 1L;
        when(policyRepository.findById(policyId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PolicyNotFoundException.class, () -> couponPolicyService.getPolicy(policyId));
    }
}

