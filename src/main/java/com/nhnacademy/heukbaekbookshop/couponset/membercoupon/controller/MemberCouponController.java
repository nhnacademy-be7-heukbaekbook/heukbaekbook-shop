package com.nhnacademy.heukbaekbookshop.couponset.membercoupon.controller;

import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.request.UseCouponRequest;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.response.MemberCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.response.UserBookCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.service.CouponIssueService;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.service.MemberCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberCouponController {
    public static final String X_USER_ID = "X-USER-ID";

    private final MemberCouponService memberCouponService;
    private final CouponIssueService couponIssueService;


    @PostMapping("/coupons/{couponId}")
    public ResponseEntity<MemberCouponResponse> issueCoupon(
            @RequestHeader(X_USER_ID) Long customerId,
            @PathVariable Long couponId
    ) {
        MemberCouponResponse response = couponIssueService.issueCouponSync(customerId, couponId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 회원 쿠폰 사용
     *
     * @param memberCouponId 회원 쿠폰 ID
     * @param useRequest     요청 본문에 포함된 주문 ID와 도서 ID
     * @return MemberCouponResponse
     */
    @PutMapping("coupons/{memberCouponId}/use")
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
     * @param customerId 회원 ID
     * @param pageable Pageable 객체
     * @return Page<MemberCouponResponse>
     */
    @GetMapping("/coupons")
    public ResponseEntity<Page<MemberCouponResponse>> getUserCoupons(
            @RequestHeader(X_USER_ID) Long customerId,
            Pageable pageable
    ) {
        Page<MemberCouponResponse> responses = memberCouponService.getUserCoupons(pageable, customerId);
        return ResponseEntity.ok(responses);
    }


    @GetMapping("/coupons/{bookId}/download-list")
    public ResponseEntity<Page<UserBookCouponResponse>> getUserCouponsDownloadList(@RequestHeader(X_USER_ID) Long customerId,
                                                                           @PathVariable Long bookId, Pageable pageable) {
        Page<UserBookCouponResponse> responses = memberCouponService.getUserBookCouponsDownloadList(customerId, bookId, pageable);
        return ResponseEntity.ok(responses);
    }

}
