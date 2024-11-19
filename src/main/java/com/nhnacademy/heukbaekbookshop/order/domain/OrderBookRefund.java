package com.nhnacademy.heukbaekbookshop.order.domain;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
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
@IdClass(OrderBookRefundPK.class)
@Table(name = "orders_books_refunds")
public class OrderBookRefund {

    @Id
    @Column(name = "book_id")
    private Long bookId;

    @Id
    @Column(name = "order_id")
    private Long orderId;

    @Id
    @Column(name = "refund_id")
    private Long refundId;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    private Book book;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    @ManyToOne
    @MapsId("refundId")
    @JoinColumn(name = "refund_id", insertable = false, updatable = false)
    private Refund refund;

    @NotNull
    @Column(name = "quantity")
    private int quantity;

}
