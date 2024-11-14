package com.nhnacademy.heukbaekbookshop.couponpolicy.controller;

import com.nhnacademy.heukbaekbookshop.couponpolicy.dto.PolicyCreateRequest;
import com.nhnacademy.heukbaekbookshop.couponpolicy.dto.PolicyDeleteRequest;
import com.nhnacademy.heukbaekbookshop.couponpolicy.dto.PolicyResponse;
import com.nhnacademy.heukbaekbookshop.couponpolicy.dto.PolicyUpdateRequest;
import com.nhnacademy.heukbaekbookshop.couponpolicy.service.CouponPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/policies")
public class CouponPolicyController {

    private final CouponPolicyService couponPolicyService;

    public CouponPolicyController(CouponPolicyService couponPolicyService) {
        this.couponPolicyService = couponPolicyService;
    }

    // 정책 생성
    @PostMapping
    public ResponseEntity<PolicyResponse> createPolicy(@RequestBody PolicyCreateRequest request) {
        PolicyResponse response = couponPolicyService.createPolicy(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 특정 정책 조회
    @GetMapping("/{id}")
    public ResponseEntity<PolicyResponse> getPolicy(@PathVariable Long id) {
        PolicyResponse response = couponPolicyService.getPolicy(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 정책 수정
    @PutMapping("/{id}")
    public ResponseEntity<PolicyResponse> updatePolicy(@PathVariable Long id, @RequestBody PolicyUpdateRequest request) {
        PolicyResponse response = couponPolicyService.updatePolicy(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 정책 삭제
    @DeleteMapping
    public ResponseEntity<Void> deletePolicy(@RequestBody PolicyDeleteRequest request) {
        couponPolicyService.deletePolicy(request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

