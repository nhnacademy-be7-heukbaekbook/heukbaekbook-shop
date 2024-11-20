package com.nhnacademy.heukbaekbookshop.couponset.coupon.domain;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "books_coupons")
public class BookCoupon extends Coupon {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    public BookCoupon(CouponPolicy couponPolicy, int availableDuration, LocalDateTime couponTimeStart, LocalDateTime couponTimeEnd, String couponName, String couponDescription, Book book) {
        super(couponPolicy, availableDuration, couponTimeStart, couponTimeEnd, couponName, couponDescription);
        this.book=book;
    }

}
