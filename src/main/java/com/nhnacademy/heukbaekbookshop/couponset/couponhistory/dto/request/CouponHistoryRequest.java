package com.nhnacademy.heukbaekbookshop.couponset.couponhistory.dto.request;

public record CouponHistoryRequest(
        Long memberCouponId,
        Long orderId,
        Long bookId
) {}
