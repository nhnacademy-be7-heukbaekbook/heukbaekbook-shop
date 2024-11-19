package com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.request;


import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record CouponRequest (
        @NotNull
        Long policyId,

        @NotNull
        int availableDuration,

        @NotNull
        LocalDateTime couponTimeStart,

        LocalDateTime couponTimeEnd,

        @NotNull
        @Length(max = 20)
        String couponName,

        @NotNull
        @Length(max = 200)
        String couponDescription
){
}
