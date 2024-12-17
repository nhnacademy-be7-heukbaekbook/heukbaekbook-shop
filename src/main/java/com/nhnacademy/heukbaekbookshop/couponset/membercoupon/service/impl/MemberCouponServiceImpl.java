package com.nhnacademy.heukbaekbookshop.couponset.membercoupon.service.impl;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.BookCoupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponStatus;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.mapper.CouponMapper;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.BookCouponRepository;
import com.nhnacademy.heukbaekbookshop.couponset.couponhistory.domain.CouponHistory;
import com.nhnacademy.heukbaekbookshop.couponset.couponhistory.repository.CouponHistoryRepository;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.domain.MemberCoupon;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.response.MemberCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.response.UserBookCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.exception.MemberCouponNotFoundException;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.repository.MemberCouponRepository;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.service.MemberCouponService;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderBook;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderBookId;
import com.nhnacademy.heukbaekbookshop.order.exception.OrderBookNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberCouponServiceImpl implements MemberCouponService {

    private final MemberCouponRepository memberCouponRepository;

    private final CouponHistoryRepository couponHistoryRepository;
    private final OrderBookRepository orderBookRepository;
    private final BookCouponRepository bookCouponRepository;


    @Override
    @Transactional
    public MemberCouponResponse useCoupon(Long memberCouponId, Long orderId, Long bookId) {
        MemberCoupon memberCoupon = memberCouponRepository.findById(memberCouponId)
                .orElseThrow(() -> new MemberCouponNotFoundException("해당 ID의 회원 쿠폰을 찾을 수 없습니다: " + memberCouponId));

        if (memberCoupon.isCouponUsed()) {
            throw new IllegalStateException("쿠폰이 이미 사용되었습니다.");
        }

        OrderBook orderBook = orderBookRepository.findById(new OrderBookId(orderId, bookId))
                        .orElseThrow(() -> new OrderBookNotFoundException("order book not found"));

        memberCoupon.markAsUsed();
        memberCouponRepository.save(memberCoupon);

        CouponHistory couponHistory = CouponMapper.toCouponHistoryEntity(memberCoupon, orderBook);
        couponHistoryRepository.save(couponHistory);

        return CouponMapper.fromMemberCouponEntity(memberCoupon);
    }

    @Override
    public Page<MemberCouponResponse> getUserCoupons(Pageable pageable, Long memberId) {
        Page<MemberCoupon> memberCoupons = memberCouponRepository.findByMemberId(pageable, memberId);
        return CouponMapper.fromMemberCouponPageableEntity(memberCoupons);
    }

    @Override
    public Page<UserBookCouponResponse> getUserBookCouponsDownloadList(Long customerId, Long bookId, Pageable pageable) {
        Page<BookCoupon> bookCoupons = bookCouponRepository.findAllByBookIdAndCouponStatus(bookId, CouponStatus.ABLE, pageable);
        return CouponMapper.fromPageableBookCoupon(bookCoupons);
    }
}

