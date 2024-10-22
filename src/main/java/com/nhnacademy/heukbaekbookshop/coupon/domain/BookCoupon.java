package com.nhnacademy.heukbaekbookshop.coupon.domain;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
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
@IdClass(BookCouponId.class)
@Table(name = "books_coupons")
public class BookCoupon {

    @Id
    @Column(name = "coupon_id")
    private long couponId;

    @Id
    @Column(name = "book_id")
    private long bookId;

    @OneToOne
    @MapsId("couponId")
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

}
