package com.nhnacademy.heukbaekbookshop.couponset.coupon.domain;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories_coupons")
public class CategoryCoupon extends Coupon {

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    @Builder
    public CategoryCoupon(CouponPolicy couponPolicy, int availableDuration, LocalDateTime couponTimeStart, LocalDateTime couponTimeEnd, String couponName, String couponDescription, Category category) {
        super(couponPolicy, availableDuration, couponTimeStart, couponTimeEnd, couponName, couponDescription);
        this.category = category;
    }

}
