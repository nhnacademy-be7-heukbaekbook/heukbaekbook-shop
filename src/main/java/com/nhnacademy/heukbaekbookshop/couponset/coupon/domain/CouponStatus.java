package com.nhnacademy.heukbaekbookshop.couponset.coupon.domain;

import lombok.Getter;

@Getter
public enum CouponStatus {
    ABLE("사용가능"),
    DISABLE("만료됨");


    private final String value;

    CouponStatus(String value) {
        this.value = value;
    }
}
