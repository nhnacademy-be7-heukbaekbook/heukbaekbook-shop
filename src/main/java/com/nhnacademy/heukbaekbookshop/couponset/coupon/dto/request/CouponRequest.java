package com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.request;


import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponType;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record CouponRequest (
        @NotNull
        Long policyId,

        int couponQuantity,

        @NotNull
        int availableDuration,

        @NotNull
        LocalDateTime couponTimeStart,

        LocalDateTime couponTimeEnd,

        @NotNull
        @Length(max = 100)
        String couponName,

        @NotNull
        @Length(max = 500)
        String couponDescription,

        CouponType couponType,

        Long bookId,

        Long categoryId
){
}
