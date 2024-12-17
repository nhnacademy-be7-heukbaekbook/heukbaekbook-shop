package com.nhnacademy.heukbaekbookshop.order.domain;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(OrderBookId.class)
@Table(name = "orders_books")
public class OrderBook {

    @Id
    @Column(name = "book_id", insertable = false, updatable = false)
    private Long bookId;

    @Id
    @Column(name = "order_id", insertable = false, updatable = false)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    @NotNull
    @Column(name = "quantity")
    private int quantity;

    @NotNull
    @Column(name = "price")
    private BigDecimal price;

    private void setOrder(Order order) {
        if (order != null) {
            this.order = order;
            order.getOrderBooks().add(this);
        }
    }

    public static OrderBook createOrderBook(Long bookId, Long orderId, Book book, Order order, int quantity, BigDecimal price) {
        OrderBook orderBook = new OrderBook();
        orderBook.bookId = bookId;
        orderBook.orderId = orderId;
        orderBook.book = book;
        orderBook.setOrder(order);
        orderBook.quantity = quantity;
        orderBook.price = price;
        return orderBook;
    }
}
