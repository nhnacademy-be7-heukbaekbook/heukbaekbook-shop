package com.nhnacademy.heukbaekbookshop.order.domain;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.member.domain.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ReviewPK.class)
@Table(name = "reviews")
public class Review {

    @Id
    @Column(name = "customer_id")
    private long customerId;

    @Id
    @Column(name = "book_id")
    private long bookId;

    @Id
    @Column(name = "order_id")
    private long orderId;

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

    @NotNull
    @Min(1)
    @Max(5)
    @Column(name = "review_score")
    private int score;

    @NotNull
    @Column(name = "review_created_at")
    private LocalDateTime createdAt;

    @Column(name = "review_updated_at")
    private LocalDateTime updatedAt;

    @NotNull
    @Length(min = 1, max = 30)
    @Column(name = "review_title")
    private String title;

    @NotNull
    @Column(name = "review_content")
    private String content;
}
