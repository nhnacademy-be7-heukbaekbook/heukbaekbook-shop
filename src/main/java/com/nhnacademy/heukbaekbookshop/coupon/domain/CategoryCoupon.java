package com.nhnacademy.heukbaekbookshop.coupon.domain;

import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CategoryCouponId.class)
@Table(name = "categories_coupons")
public class CategoryCoupon {

    @Id
    @Column(name = "coupon_id")
    private long couponId;

    @Id
    @Column(name = "category_id")
    private long categoryId;

    @OneToOne
    @MapsId("couponId")
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;

}
