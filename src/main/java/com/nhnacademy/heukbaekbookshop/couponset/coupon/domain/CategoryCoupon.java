package com.nhnacademy.heukbaekbookshop.couponset.coupon.domain;

import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponType;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
                          int couponQuantity,
                          int availableDuration,
                          LocalDateTime couponTimeStart,
                          LocalDateTime couponTimeEnd,
                          String couponName,
                          String couponDescription,
                          Category category
    ) {
        super(couponPolicy, couponQuantity, availableDuration, couponTimeStart, couponTimeEnd, couponName, couponDescription, CouponType.CATEGORY);
        this.category = category;
    }

}
