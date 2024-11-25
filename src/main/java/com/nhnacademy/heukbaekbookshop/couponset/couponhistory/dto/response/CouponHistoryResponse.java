package com.nhnacademy.heukbaekbookshop.couponset.couponhistory.dto.response;

import java.time.LocalDateTime;

public record CouponHistoryResponse(
        Long couponHistoryId,
        Long memberCouponId,
        Long memberId,
        Long couponId,
        LocalDateTime usedAt,
        Long bookId,
        Long orderId
) {}


