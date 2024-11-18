package com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response;

import java.time.LocalDateTime;

public record CouponResponse(
        Long couponId,
        Long couponType,
        LocalDateTime expirationDate,
        int availableDuration,
        String title,
        String categoryName
) {
//    public static CouponInfoResponse from(Coupon coupon) {
//        return new CouponInfoResponse(
//                coupon.getId(),
//                coupon.getCouponType() != null ? coupon.getCouponType().getId() : null,
//                coupon.getExpirationDate(),
//                coupon.getAvailableDuration(),
//                coupon.getBookCoupon() != null ? coupon.getBookCoupon().getBook().getTitle() : null,
//                coupon.getCategoryCoupon() != null ? coupon.getCategoryCoupon().getCategory().getName() : null
//        );
//    }
}
