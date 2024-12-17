package com.nhnacademy.heukbaekbookshop.couponset.membercoupon.service;

import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.request.CouponIssueRequest;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.response.MemberCouponResponse;

public interface CouponIssueService {

    MemberCouponResponse issueCouponSync(Long memberId, Long couponId);
    MemberCouponResponse issueCouponAsync(CouponIssueRequest couponIssueRequest);
}
