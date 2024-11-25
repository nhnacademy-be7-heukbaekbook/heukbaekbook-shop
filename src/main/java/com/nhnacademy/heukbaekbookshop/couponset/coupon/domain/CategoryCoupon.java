package com.nhnacademy.heukbaekbookshop.couponset.coupon.domain;

import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "categories_coupons")
public class CategoryCoupon extends Coupon {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


    public CategoryCoupon(CouponPolicy couponPolicy,
                          int availableDuration,
                          LocalDateTime couponTimeStart,
                          LocalDateTime couponTimeEnd,
                          String couponName,
                          String couponDescription,
                          Category category
    ) {
        super(couponPolicy, availableDuration, couponTimeStart, couponTimeEnd, couponName, couponDescription);
        this.category = category;
    }

}
