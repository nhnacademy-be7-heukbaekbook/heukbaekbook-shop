package com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.mapper;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.CouponHistory;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponHistoryResponse;

public class CouponHistoryMapper {
    public static CouponHistoryResponse toResponse(CouponHistory history) {
        return new CouponHistoryResponse(
                history.getId(),
                history.getMemberCoupon().getId(),
                history.getUsedAt(),
                history.getOrderBook().getBookId(),
                history.getOrderBook().getOrderId()
        );
    }
}


