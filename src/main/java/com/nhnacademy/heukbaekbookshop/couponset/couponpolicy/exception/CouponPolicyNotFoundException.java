package com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CouponPolicyNotFoundException extends RuntimeException {
    public CouponPolicyNotFoundException(String message) {
        super(message);
    }
}
