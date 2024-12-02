package com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.request;


import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponType;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public record CouponRequest (
        @NotNull
        Long policyId,

        Integer couponQuantity,

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

)implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
}
