package com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.CouponHistory;

import java.time.LocalDateTime;

public record CouponHistoryResponse(
        Long historyId,
        LocalDateTime couponUsedAt,
        Long bookId,
        Long orderId
) {
    public static CouponHistoryResponse from(CouponHistory usageHistory) {
        return new CouponHistoryResponse(
                usageHistory.getId(),
                usageHistory.getUsedAt(),
                usageHistory.getOrderBook() != null ? usageHistory.getOrderBook().getBook().getId() : null,
                usageHistory.getOrderBook() != null ? usageHistory.getOrderBook().getOrder().getId() : null
        );
    }
}
