package com.nhnacademy.heukbaekbookshop.order.domain;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(OrderBookId.class)
@Table(name = "packaging")
public class Packaging {

    @Id
    @Column(name = "book_id")
    private Long bookId;

    @Id
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wrapping_paper_id")
    private WrappingPaper wrappingPaper;

    @NotNull
    @Column(name = "packaging_price")
    private BigDecimal price;

    public static Packaging createPackaging(Book book, Order order, WrappingPaper wrappingPaper, BigDecimal price) {
        Packaging packaging = new Packaging();
        packaging.bookId = book.getId();
        packaging.orderId = order.getId();
        packaging.book = book;
        packaging.order = order;
        packaging.wrappingPaper = wrappingPaper;
        packaging.price = price;
        return packaging;
    }
}
