package com.nhnacademy.heukbaekbookshop.couponset.coupon.service.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookNotFoundException;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.category.exception.CategoryNotFoundException;
import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepository;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.BookCoupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.CouponStatus;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.mapper.CouponMapper;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.request.CouponRequest;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.BookCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CategoryCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.exception.CouponNotFoundException;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.service.CouponService;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.exception.CouponPolicyNotFoundException;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.repository.CouponPolicyRepository;
import lombok.RequiredArgsConstructor;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.CouponRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponPolicyRepository couponPolicyRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CouponResponse createCoupon(CouponRequest couponRequest) {
        CouponPolicy couponPolicy = couponPolicyRepository.findById(couponRequest.policyId())
                .orElseThrow(CouponPolicyNotFoundException::new);

        Coupon coupon = null;
        if (Objects.nonNull(couponRequest.bookId())) {
            Book book = bookRepository.findById(couponRequest.bookId()).orElseThrow(BookNotFoundException::new);
            coupon = CouponMapper.toBookCouponEntity(couponRequest, couponPolicy, book);
        } else if (Objects.nonNull(couponRequest.categoryId())) {
            Category category = categoryRepository.findById(couponRequest.categoryId()).orElseThrow(CategoryNotFoundException::new);
            coupon = CouponMapper.toCategoryCouponEntity(couponRequest, couponPolicy, category);
        } else {
            coupon = CouponMapper.toEntity(couponRequest, couponPolicy);
        }

        return CouponMapper.fromEntity(
                couponRepository.save(coupon)
        );
    }

    @Override
    public CouponResponse getCoupon(Long couponId) {
        return CouponMapper.fromEntity(
                couponRepository.findById(couponId)
                        .orElseThrow(CouponNotFoundException::new)
        );
    }

    @Override
    public Page<CouponResponse> getAllNormalCoupons(Pageable pageable) {
        Page<Coupon> coupons = couponRepository.findAllNormalCoupons(pageable);
        return CouponMapper.fromPageableEntity(coupons, pageable);
    }

    @Override
    public Page<BookCouponResponse> getAllBookCoupons(Pageable pageable) {
        return couponRepository.findAllBookCoupons(pageable);
    }

    @Override
    public Page<CategoryCouponResponse> getAllCategoryCoupons(Pageable pageable) {
        return couponRepository.findAllCategoryCoupons(pageable);
    }

    @Override
    public Page<CouponResponse> getCouponsByType(DiscountType discountType, Pageable pageable) {
        Page<Coupon> coupons = couponRepository.findAllByDiscountType(discountType, pageable);
        return CouponMapper.fromPageableEntity(coupons, pageable);
    }

    @Override
    public Page<CouponResponse> getCouponsByStatus(CouponStatus couponStatus, Pageable pageable) {
        Page<Coupon> coupons = couponRepository.findAllByCouponStatus(couponStatus, pageable);
        return CouponMapper.fromPageableEntity(coupons, pageable);
    }

    @Override
    @Transactional
    public CouponResponse updateCoupon(Long couponId, CouponRequest couponRequest) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(CouponNotFoundException::new);
        CouponPolicy couponPolicy = couponPolicyRepository.findById(couponRequest.policyId()).orElseThrow(CouponPolicyNotFoundException::new);

        return CouponMapper.fromEntity(
                coupon.modifyCoupon(
                        couponPolicy,
                        couponRequest.availableDuration(),
                        couponRequest.couponTimeStart(),
                        couponRequest.couponTimeEnd(),
                        couponRequest.couponName(),
                        couponRequest.couponDescription()
                )
        );
    }

    @Override
    @Transactional
    public void changeCouponStatus(Long couponId, CouponStatus couponStatus) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(CouponNotFoundException::new);
        coupon.setCouponStatus(couponStatus);

//        couponRepository.delete(
//                couponRepository.findById(couponId)
//                        .orElseThrow(CouponNotFoundException::new)
//        );
    }
}
