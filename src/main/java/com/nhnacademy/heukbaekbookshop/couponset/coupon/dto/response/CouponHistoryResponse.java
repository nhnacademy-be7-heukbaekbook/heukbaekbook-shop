package com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response;

import java.time.LocalDateTime;

public record CouponHistoryResponse(
        Long couponHistoryId, // 쿠폰 사용 내역 ID
        Long memberCouponId,  // 회원 쿠폰 ID
        LocalDateTime usedAt, // 쿠폰 사용일
        Long bookId,          // 사용한 도서 번호
        Long orderId          // 사용한 주문 번호
) {}

