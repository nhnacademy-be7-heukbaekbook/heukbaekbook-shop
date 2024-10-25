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
@Table(name = "categories_coupons")
public class CategoryCoupon {

    @Id
    @Column(name = "coupon_id")
    private Long couponId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "coupon_id", insertable = false, updatable = false)
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
