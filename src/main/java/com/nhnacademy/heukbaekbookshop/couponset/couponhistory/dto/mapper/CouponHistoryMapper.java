package com.nhnacademy.heukbaekbookshop.couponset.couponhistory.dto.mapper;

import com.nhnacademy.heukbaekbookshop.couponset.couponhistory.domain.CouponHistory;
import com.nhnacademy.heukbaekbookshop.couponset.couponhistory.dto.response.CouponHistoryResponse;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.domain.MemberCoupon;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderBook;

import java.time.LocalDateTime;

public class CouponHistoryMapper {
    public static CouponHistoryResponse toResponse(CouponHistory couponHistory) {
        return new CouponHistoryResponse(
                couponHistory.getId(),
                couponHistory.getMemberCoupon().getId(),
                couponHistory.getMemberCoupon().getMember().getId(),
                couponHistory.getMemberCoupon().getCoupon().getId(),
                couponHistory.getUsedAt(),
                couponHistory.getOrderBook().getBook().getId(),
                couponHistory.getOrderBook().getOrder().getId()
        );
    }

    public static CouponHistory toCouponHistoryEntity(MemberCoupon memberCoupon, OrderBook orderBook) {
        return new CouponHistory(
                null,
                memberCoupon,
                LocalDateTime.now(),
                orderBook,
                memberCoupon.getCoupon()
        );
    }

}
