package com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.request;

import java.time.LocalDateTime;

public record CouponIssueRequest(
        Long couponId,
        Long customerId,
        LocalDateTime couponExpirationDate
){
}
