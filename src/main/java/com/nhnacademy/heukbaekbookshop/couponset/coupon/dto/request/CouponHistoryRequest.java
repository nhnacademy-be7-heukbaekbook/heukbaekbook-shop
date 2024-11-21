package com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.request;

public record CouponHistoryRequest(
        Long memberCouponId,
        Long orderId,
        Long bookId
) {}
