package com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.controller;

import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyRequest;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyResponse;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.service.CouponPolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * CouponPolicyController(쿠폰 정책) RestController
 *
 * @author : 이승형
 * @date : 2024-11-19
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/coupons/policy")
public class CouponPolicyController {

    private final CouponPolicyService couponPolicyService;

    /**
     * 쿠폰 정책 생성 요청 시 사용되는 메서드입니다.
     *
     * @param couponPolicyRequest 쿠폰 정책 생성 dto 입니다.
     * @return 성공시, 응답코드 201 반환합니다.
     */
    @PostMapping
    public ResponseEntity<CouponPolicyResponse> createCouponPolicy(@Valid @RequestBody CouponPolicyRequest couponPolicyRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(couponPolicyService.createCouponPolicy(couponPolicyRequest)
                );
    }

    /**
     * 쿠폰 정책 단일 조회 요청 시 사용되는 메서드입니다.
     *
     * @param policyId  쿠폰 정책 조회 id 입니다.
     * @return  성공시, 응답코드 200 반환합니다.
     */
    @GetMapping("/{policyId}")
    public ResponseEntity<CouponPolicyResponse> getCouponPolicy(@PathVariable Long policyId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponPolicyService.getCouponPolicyById(policyId)
                );
    }

    /**
     * 쿠폰 정책 전체 조회 요청 시 사용되는 메서드입니다.
     *
     * @param pageable Page 처리 용 pageble 객체입니다.
     * @return  성공시, 응답코드 200 반환합니다.
     */
    @GetMapping
    public ResponseEntity<Page<CouponPolicyResponse>> getCouponPolicies(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponPolicyService.getAllCouponPolicies(pageable));
    }

    /**
     * 쿠폰 정책 전체 조회 요청 시 사용되는 메서드입니다.
     * 쿠폰 등록 시 select-box에서 사용됩니다.
     *
     * @return 성공시, 응답코드 200 반환합니다.
     */
    @GetMapping("/list")
    public ResponseEntity<List<CouponPolicyResponse>> getCouponPolicyList() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponPolicyService.getAllCouponPolicyList());
    }

    /**
     * 쿠폰 정책 수정 요청 시 사용되는 메서드입니다.
     *
     * @param policyId  쿠폰 정책 수정 id 입니다.
     * @param couponPolicyRequest 쿠폰 정책 수정 dto 입니다.
     * @return 성공시, 응답코드 200 반환합니다.
     */
    @PutMapping("/{policyId}")
    public ResponseEntity<CouponPolicyResponse> updateCouponPolicy(@PathVariable Long policyId, @Valid @RequestBody CouponPolicyRequest couponPolicyRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponPolicyService.updateCouponPolicy(policyId, couponPolicyRequest));
    }

    /**
     * 쿠폰 정책 삭제 요청 시 사용되는 메서드입니다.
     *
     * @param policyId 쿠폰 정책 삭제 id 입니다.
     * @return 성공시, 응답코드 204 반환합니다.
     */
    @DeleteMapping("/{policyId}")
    public ResponseEntity<Void> deleteCouponPolicy(@PathVariable Long policyId) {
        couponPolicyService.deleteCouponPolicy(policyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Batch 전용(Admin전용)
     *
     * @param type discountType
     * @param amount discountAmount
     * @return couponPolicyResponse
     */
    @GetMapping("/{type}/{amount}")
    public ResponseEntity<CouponPolicyResponse> getCouponPolicyByTypeAndAmount(@PathVariable String type, @PathVariable BigDecimal amount) {
        DiscountType discountType = (type.equals(DiscountType.FIXED.name())) ? DiscountType.FIXED : DiscountType.PERCENTAGE;
        CouponPolicyResponse response = couponPolicyService.getCouponPolicyByDiscountTypeAndDiscountAmount(discountType, amount);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
