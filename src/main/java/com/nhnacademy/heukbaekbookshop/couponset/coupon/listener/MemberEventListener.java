package com.nhnacademy.heukbaekbookshop.couponset.coupon.listener;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponType;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.service.CouponService;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.service.CouponIssueService;
import com.nhnacademy.heukbaekbookshop.point.history.event.SignupEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MemberEventListener {
    private final CouponService couponService;
    private final CouponIssueService couponIssueService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSignupEvent(SignupEvent signupEvent) {
        processIssued(CouponType.WELCOME, signupEvent.customerId());
    }

    private void processIssued(CouponType couponType, Long customerId) {
        Long welcomeCouponId = couponService.getCouponIdByCouponType(couponType);
        couponIssueService.issueCouponSync(customerId, welcomeCouponId);
    }
}
