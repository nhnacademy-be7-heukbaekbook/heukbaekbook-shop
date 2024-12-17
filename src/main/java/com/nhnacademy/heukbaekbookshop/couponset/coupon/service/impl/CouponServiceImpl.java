package com.nhnacademy.heukbaekbookshop.couponset.coupon.service.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookNotFoundException;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.category.exception.CategoryNotFoundException;
import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepository;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponStatus;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponType;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.mapper.CouponMapper;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.request.CouponRequest;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.BookCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CategoryCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponPageResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.exception.CouponNotFoundException;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.CouponRepository;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.service.CouponService;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyResponse;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.mapper.CouponPolicyMapper;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.exception.CouponPolicyNotFoundException;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.repository.CouponPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponPolicyRepository couponPolicyRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final CouponRedisService couponRedisService;

    @Override
    @Transactional
    public CouponResponse createCoupon(CouponRequest couponRequest) {
        CouponPolicy couponPolicy = couponPolicyRepository.findById(couponRequest.policyId())
                .orElseThrow(CouponPolicyNotFoundException::new);

        Coupon coupon = null;
        if (couponRequest.couponType().equals(CouponType.BOOK)) {
            Book book = bookRepository.findById(couponRequest.bookId()).orElseThrow(BookNotFoundException::new);
            coupon = CouponMapper.toBookCouponEntity(couponRequest, couponPolicy, book);
        } else if (couponRequest.couponType().equals(CouponType.CATEGORY)) {
            Category category = categoryRepository.findById(couponRequest.categoryId()).orElseThrow(CategoryNotFoundException::new);
            coupon = CouponMapper.toCategoryCouponEntity(couponRequest, couponPolicy, category);
        } else if(!couponRequest.couponType().equals(CouponType.GENERAL)
                && couponRepository.existsByCouponType(couponRequest.couponType())){    // WELCOME, BIRTHDAY Coupon
            // Already existing Coupon Setting CouponStatus DISABLED
            couponRepository.findByCouponType(couponRequest.couponType()).setCouponStatus(CouponStatus.DISABLE);
            coupon = CouponMapper.toEntity(couponRequest, couponPolicy);
        }else{
            coupon = CouponMapper.toEntity(couponRequest, couponPolicy);
        }

        CouponResponse couponResponse = CouponMapper.fromEntity(
                couponRepository.save(coupon)
        );

        if (couponRequest.couponQuantity() > 0) {
            couponRedisService.saveCouponToRedis(couponResponse);
        }

        return couponResponse;
    }

    @Override
    public CouponResponse getCoupon(Long couponId) {
        return CouponMapper.fromEntity(
                couponRepository.findById(couponId)
                        .orElseThrow(CouponNotFoundException::new)
        );
    }

    @Override
    public List<CouponResponse> getDownloadableCoupons(Long bookId) {
        List<Coupon> downloadableCoupons = couponRepository.findDownloadableCouponsByBookId(bookId);

        return downloadableCoupons.stream()
                .map(CouponMapper::fromEntity)
                .toList();
    }

    @Override
    public Page<CouponResponse> getAllNormalCoupons(Pageable pageable) {
        Page<Coupon> coupons = couponRepository.findAllNormalCoupons(pageable);
        return CouponMapper.fromPageableEntity(coupons);
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
        return CouponMapper.fromPageableEntity(coupons);
    }

    @Override
    public Page<CouponResponse> getCouponsByStatus(CouponStatus couponStatus, Pageable pageable) {
        Page<Coupon> coupons = couponRepository.findAllByCouponStatus(couponStatus, pageable);
        return CouponMapper.fromPageableEntity(coupons);
    }

    @Override
    @Transactional
    public CouponResponse updateCoupon(Long couponId, CouponRequest couponRequest) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(CouponNotFoundException::new);
        CouponPolicy couponPolicy = couponPolicyRepository.findById(couponRequest.policyId()).orElseThrow(CouponPolicyNotFoundException::new);

        CouponResponse couponResponse = CouponMapper.fromEntity(
                coupon.modifyCoupon(
                        couponPolicy,
                        couponRequest.couponQuantity(),
                        couponRequest.availableDuration(),
                        couponRequest.couponTimeStart(),
                        couponRequest.couponTimeEnd(),
                        couponRequest.couponName(),
                        couponRequest.couponDescription()
                )
        );

        if (couponRequest.couponQuantity() > 0) {
            couponRedisService.saveCouponToRedis(couponResponse);
        }

        return couponResponse;
    }

    @Override
    @Transactional
    public void changeCouponStatus(Long couponId, CouponStatus couponStatus) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(CouponNotFoundException::new);
        coupon.setCouponStatus(couponStatus);

        if (couponStatus == CouponStatus.DISABLE) {
            couponRedisService.deleteCouponFromRedis(couponId);
        }
    }

    @Override
    public CouponPageResponse getCouponPageResponse(Long customerId, Pageable pageable) {
        Page<CouponResponse> normalCoupons = CouponMapper.fromPageableEntity(couponRepository.findAllNormalCoupons(pageable)) ;
        Page<BookCouponResponse> bookCoupons = couponRepository.findAllBookCoupons(pageable);
        Page<CategoryCouponResponse> categoryCoupons = couponRepository.findAllCategoryCoupons(pageable);
        List<CouponPolicyResponse> couponPolicyList = CouponPolicyMapper.fromEntityList(couponPolicyRepository.findAllByOrderByDiscountTypeAscDiscountAmountAsc());
        return CouponMapper.toCouponPageResponse(normalCoupons,bookCoupons,categoryCoupons,couponPolicyList);
    }

    @Override
    public Long getCouponIdByCouponType(CouponType couponType) {
        return couponRepository.findAvailableCouponIdByCouponType(couponType)
                .orElseThrow(CouponNotFoundException::new);

    }
}
