package com.nhnacademy.heukbaekbookshop.cart.domain;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CartId.class)
@Table(name = "carts")
public class Cart {

    @Id
    @Column(name = "book_id")
    private long bookId;

    @Id
    @Column(name = "customer_id")
    private long customerId;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @OneToOne
    @MapsId("customerId")
    @JoinColumn(name = "customer_id")
    private Member member;

    @NotNull
    @Column(name = "book_amount")
    private int amount;
}
