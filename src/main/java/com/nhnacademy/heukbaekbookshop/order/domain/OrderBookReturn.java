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
@IdClass(OrderBookReturnId.class)
@Table(name = "orders_books_returns")
public class OrderBookReturn {

    @Id
    @Column(name = "book_id")
    private long bookId;

    @Id
    @Column(name = "order_id")
    private long orderId;

    @Id
    @Column(name = "return_id")
    private long returnId;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @MapsId("returnId")
    @JoinColumn(name = "return_id")
    private Return returns;

    @NotNull
    @Column(name = "quantity")
    private int quantity;

}
