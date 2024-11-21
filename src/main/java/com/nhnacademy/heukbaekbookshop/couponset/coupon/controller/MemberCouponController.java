package com.nhnacademy.heukbaekbookshop.couponset.coupon.controller;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.request.UseCouponRequest;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponHistoryResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.MemberCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.service.MemberCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/coupons")
public class MemberCouponController {
    public static final String X_USER_ID = "X-USER-ID";

    private final MemberCouponService memberCouponService;


    @PostMapping("/{couponId}")
    public ResponseEntity<MemberCouponResponse> issueCoupon(
            @RequestHeader(X_USER_ID) Long customerId,
            @PathVariable Long couponId
    ) {
        MemberCouponResponse response = memberCouponService.issueCoupon(customerId, couponId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 회원 쿠폰 사용
     *
     * @param memberCouponId 회원 쿠폰 ID
     * @param useRequest 요청 본문에 포함된 주문 ID와 도서 ID
     * @return MemberCouponResponse
     */
    @PutMapping("/{memberCouponId}/use")
    public ResponseEntity<MemberCouponResponse> useCoupon(
            @PathVariable Long memberCouponId,
            @RequestBody UseCouponRequest useRequest) {

        MemberCouponResponse response = memberCouponService.useCoupon(
                memberCouponId,
                useRequest.orderId(),
                useRequest.bookId()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * 회원의 쿠폰 조회
     *
     * @param memberId 회원 ID
     * @param pageable Pageable 객체
     * @return Page<MemberCouponResponse>
     */
    @GetMapping("/{memberId}")
    public ResponseEntity<Page<MemberCouponResponse>> getUserCoupons(
            @PathVariable Long memberId,
            Pageable pageable
    ) {
        Page<MemberCouponResponse> responses = memberCouponService.getUserCoupons(pageable, memberId);
        return ResponseEntity.ok(responses);
    }
}
