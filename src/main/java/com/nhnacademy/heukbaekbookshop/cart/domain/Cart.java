package com.nhnacademy.heukbaekbookshop.cart.domain;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(CartId.class)
@Table(name = "carts")
public class Cart {
    @Id
    @Column(name = "book_id", insertable = false, updatable = false)
    private Long bookId;

    @Id
    @Column(name = "customer_id", insertable = false, updatable = false)
    private Long customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "book_id", insertable = false, updatable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", insertable = false, updatable = false)
    private Member member;

    @Column(name = "book_amount")
    @Setter
    private int amount;

    public static Cart createCart(Long bookId, Long customerId, Book book, Member member, int amount) {
        Cart cart = new Cart();
        cart.bookId = bookId;
        cart.customerId = customerId;
        cart.book = book;
        cart.member = member;
        cart.amount = amount;
        return cart;
    }
}
