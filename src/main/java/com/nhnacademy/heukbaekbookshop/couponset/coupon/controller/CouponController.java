package com.nhnacademy.heukbaekbookshop.couponset.coupon.controller;


import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponStatus;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.request.CouponRequest;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.BookCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CategoryCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponPageResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.service.CouponService;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * CouponController(쿠폰) RestController
 *
 * @author : 이승형
 * @date : 2024-11-20
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/coupons")
public class CouponController {
    public static final String X_USER_ID = "X-USER-ID";
    private final CouponService couponService;

    /**
     * 쿠폰 단일 조회 요청 시 사용되는 메서드입니다.
     *
     * @param couponId 쿠폰 조회 id 입니다.
     * @return 성공시, 응답코드 200 반환합니다.
     */
    @GetMapping("/{couponId}")
    public ResponseEntity<CouponResponse> getCoupon(@PathVariable Long couponId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponService.getCoupon(couponId)
                );
    }

    /**
     * 쿠폰 전체 조회 기본 요청 시 사용되는 메서드입니다.
     * 도서, 카테고리 쿠폰이 아닌 쿠폰 리스트입니다.
     *
     * @param pageable Page 처리 용 pageble 객체입니다.
     * @return 성공시, 응답코드 200 반환합니다.
     */
    @GetMapping
    public ResponseEntity<Page<CouponResponse>> getAllNormalCoupons(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponService.getAllNormalCoupons(pageable)
                );
    }

    /**
     * 쿠폰 전체 조회 기본 요청 시 사용되는 메서드입니다.
     * 도서 쿠폰 리스트입니다.
     *
     * @param pageable Page 처리 용 pageble 객체입니다.
     * @return 성공시, 응답코드 200 반환합니다.
     */
    @GetMapping("/book-coupon")
    public ResponseEntity<Page<BookCouponResponse>> getAllBookCoupons(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponService.getAllBookCoupons(pageable)
                );
    }

    /**
     * 쿠폰 전체 조회 기본 요청 시 사용되는 메서드입니다.
     * 카테고리 쿠폰 리스트입니다.
     *
     * @param pageable Page 처리 용 pageble 객체입니다.
     * @return 성공시, 응답코드 200 반환합니다.
     */
    @GetMapping("/category-coupon")
    public ResponseEntity<Page<CategoryCouponResponse>> getAllCategoryCoupons(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponService.getAllCategoryCoupons(pageable)
                );
    }

    /**
     * 쿠폰 전체 조회 상태 필터링 요청 시 사용되는 메서드입니다.
     *
     * @param couponStatus  필터링용 쿠폰 상태입니다.
     * @param pageable Page 처리 용 pageble 객체입니다.
     * @return 성공시, 응답코드 200 반환합니다.
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<CouponResponse>> getCouponsByStatus(@PathVariable("status") CouponStatus couponStatus, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponService.getCouponsByStatus(couponStatus, pageable)
                );
    }

    /**
     * 쿠폰 전체 조회 할인 타입 필터링 요청 시 사용되는 메서드입니다.
     *
     * @param discountType 필터링용 할인 타입입니다.
     * @param pageable Page 처리 용 pageble 객체입니다.
     * @return 성공시, 응답코드 200 반환합니다.
     */
    @GetMapping("/type/discount/{type}")
    public ResponseEntity<Page<CouponResponse>> getCouponsByType(@PathVariable("type") DiscountType discountType, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponService.getCouponsByType(discountType,pageable));
    }

    /**
     * 쿠폰 생성 요청 시 사용되는 메서드입니다.
     *
     * @param couponRequest 쿠폰 생성 dto 입니다.
     * @return  성공시, 응답코드 201 반환합니다.
     */
    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(@Valid @RequestBody CouponRequest couponRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(couponService.createCoupon(couponRequest)
                );
    }

    /**
     * 쿠폰 수정 요청 시 사용되는 메서드입니다.
     *
     * @param couponId  쿠폰 수정 id 입니다.
     * @param couponRequest 쿠폰 수정 dto 입니다.
     * @return 성공시, 응답코드 200 반환합니다.
     */
    @PutMapping("/{couponId}")
    public ResponseEntity<CouponResponse> updateCoupon(@PathVariable("couponId") Long couponId, @Valid @RequestBody CouponRequest couponRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponService.updateCoupon(couponId, couponRequest)
                );
    }

    /**
     * 쿠폰 삭제 요청 시 사용되는 메서드입니다.
     *
     * @param couponId 쿠폰 삭제 id 입니다.
     * @return 성공시, 응답코드 204 반환합니다.
     */
    @DeleteMapping("/{couponId}")
    public ResponseEntity<Void> changeIssuedCoupon(@PathVariable("couponId") Long couponId) {
        couponService.changeCouponStatus(couponId, CouponStatus.DISABLE);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/coupon-page")
    public ResponseEntity<CouponPageResponse> getCouponPage(@RequestHeader(X_USER_ID) Long customerId, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponService.getCouponPageResponse(customerId, pageable));
    }

}
