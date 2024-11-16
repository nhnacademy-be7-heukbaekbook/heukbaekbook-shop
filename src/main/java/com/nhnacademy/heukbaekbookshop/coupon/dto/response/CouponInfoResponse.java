package com.nhnacademy.heukbaekbookshop.coupon.dto.response;

import com.nhnacademy.heukbaekbookshop.coupon.domain.Coupon;
import java.time.LocalDateTime;

public record CouponInfoResponse(
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
