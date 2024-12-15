package com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.mapper;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.BookCoupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.CategoryCoupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponStatus;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponType;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.request.CouponRequest;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.BookCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CategoryCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponPageResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.couponhistory.domain.CouponHistory;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyResponse;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.mapper.CouponPolicyMapper;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.domain.MemberCoupon;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.response.MemberCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.response.UserBookCouponResponse;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.mapper.MemberMapper;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderBook;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponMapper {

    public static CouponResponse fromEntity(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                CouponPolicyMapper.fromEntity(coupon.getCouponPolicy()),
                coupon.getCouponStatus(),
                coupon.getCouponQuantity(),
                coupon.getAvailableDuration(),
                coupon.getCouponTimeStart(),
                coupon.getCouponTimeEnd(),
                coupon.getCouponName(),
                coupon.getCouponDescription(),
                coupon.getCouponCreatedAt(),
                coupon.getCouponType()
        );
    }

    public static Page<CouponResponse> fromPageableEntity(Page<Coupon> coupons) {
        return coupons.map(CouponMapper::fromEntity);
    }

    public static Coupon toEntity(CouponRequest couponRequest, CouponPolicy couponPolicy) {
        return Coupon.builder()
                .couponPolicy(couponPolicy)
                .couponQuantity(couponRequest.couponQuantity())
                .availableDuration(couponRequest.availableDuration())
                .couponTimeStart(couponRequest.couponTimeStart())
                .couponTimeEnd(couponRequest.couponTimeEnd())
                .couponName(couponRequest.couponName())
                .couponDescription(couponRequest.couponDescription())
                .couponType(couponRequest.couponType())
                .build();
    }

    public static BookCoupon toBookCouponEntity(CouponRequest couponRequest, CouponPolicy couponPolicy, Book book) {
        int requestCouponQuantity = (couponRequest.couponQuantity() == null) ? -1 : couponRequest.couponQuantity();

        return new BookCoupon(couponPolicy,
                requestCouponQuantity,
                couponRequest.availableDuration(),
                couponRequest.couponTimeStart(),
                couponRequest.couponTimeEnd(),
                couponRequest.couponName(),
                couponRequest.couponDescription(),
                book
        );
    }

    public static UserBookCouponResponse toUserBookCouponResponse(BookCoupon bookCoupon) {
        return new UserBookCouponResponse(
                bookCoupon.getId(),
                CouponPolicyMapper.fromEntity(bookCoupon.getCouponPolicy()),
                bookCoupon.getCouponType(),
                bookCoupon.getCouponTimeStart(),
                bookCoupon.getCouponTimeEnd()
                );
    }

    public static Page<UserBookCouponResponse> fromPageableBookCoupon(Page<BookCoupon> bookCoupons) {
        return bookCoupons.map(CouponMapper::toUserBookCouponResponse);
    }

    public static CategoryCoupon toCategoryCouponEntity(CouponRequest couponRequest, CouponPolicy couponPolicy, Category category) {
        return new CategoryCoupon(couponPolicy,
                couponRequest.couponQuantity(),
                couponRequest.availableDuration(),
                couponRequest.couponTimeStart(),
                couponRequest.couponTimeEnd(),
                couponRequest.couponName(),
                couponRequest.couponDescription(),
                category);
    }

    public static MemberCoupon toMemberCouponEntity(Member member, Coupon coupon, LocalDateTime couponExpirationDate) {
        return MemberCoupon.builder()
                .member(member)
                .coupon(coupon)
                .expirationDate(couponExpirationDate).build();
    }

    public static MemberCouponResponse fromMemberCouponEntity(MemberCoupon memberCoupon) {

        return new MemberCouponResponse(
                CouponMapper.fromEntity(memberCoupon.getCoupon()),
                MemberMapper.createMemberResponse(memberCoupon.getMember()),
                memberCoupon.isCouponUsed(),
                memberCoupon.getCouponIssuedAt(),
                memberCoupon.getCouponExpirationDate()
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


    public static Page<MemberCouponResponse> fromMemberCouponPageableEntity(Page<MemberCoupon> memberCoupons) {
        return memberCoupons.map(CouponMapper::fromMemberCouponEntity);
    }

    public static CouponPageResponse toCouponPageResponse(Page<CouponResponse> normalCoupons,
                                                          Page<BookCouponResponse> bookCoupons,
                                                          Page<CategoryCouponResponse> categoryCoupons,
                                                          List<CouponPolicyResponse> couponPolicyList
    ) {
        return new CouponPageResponse(
                normalCoupons,
                bookCoupons,
                categoryCoupons,
                couponPolicyList,
                DiscountType.values(),
                CouponStatus.values(),
                new CouponType[]{CouponType.GENERAL, CouponType.BIRTHDAY, CouponType.WELCOME}
        );
    }
}


