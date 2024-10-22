package com.nhnacademy.heukbaekbookshop.order.domain;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(OrderBookId.class)
@Table(name = "packaging")
public class Package {

    @Id
    @Column(name = "book_id")
    private long bookId;

    @Id
    @Column(name = "order_id")
    private long orderId;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "wrapping_paper_id")
    private WrappingPaper wrappingPaper;

    @NotNull
    @Column(name = "packaging_price")
    private BigDecimal packagingPrice;
}
