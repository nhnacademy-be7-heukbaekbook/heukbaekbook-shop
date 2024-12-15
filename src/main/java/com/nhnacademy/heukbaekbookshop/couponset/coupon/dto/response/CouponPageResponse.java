package com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponStatus;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponType;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyResponse;
import org.springframework.data.domain.Page;

import java.util.List;
public record CouponPageResponse (
        Page<CouponResponse> normalCoupons,
        Page<BookCouponResponse> bookCoupons,
        Page<CategoryCouponResponse> categoryCoupons,
        List<CouponPolicyResponse> couponPolicyList,
        DiscountType [] discountType,
        CouponStatus[] couponstatus,
        CouponType[] couponType
){
}
