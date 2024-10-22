package com.nhnacademy.heukbaekbookshop.image.domain;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.member.domain.Customer;
import com.nhnacademy.heukbaekbookshop.order.domain.Order;
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
@IdClass(ReviewImageId.class)
@Table(name = "reviews_images")
public class ReviewImage {
    @Id
    @Column(name = "image_id")
    private long imageId;

    @Id
    @Column(name = "customer_id")
    private long customerId;

    @Id
    @Column(name = "book_id")
    private long bookId;

    @Id
    @Column(name = "order_id")
    private long orderId;

    @OneToOne
    @MapsId("imageId")
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne
    @MapsId("customerId")
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

}
