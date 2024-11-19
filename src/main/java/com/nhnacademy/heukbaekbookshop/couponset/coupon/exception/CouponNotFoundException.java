package com.nhnacademy.heukbaekbookshop.couponset.coupon.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CouponNotFoundException extends RuntimeException {
    public CouponNotFoundException(String message) {
        super(message);
    }
}
