package com.nhnacademy.heukbaekbookshop.book.domain;

import com.nhnacademy.heukbaekbookshop.member.domain.Customer;
import com.nhnacademy.heukbaekbookshop.member.domain.Member;
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
@IdClass(LikeId.class)
@Table(name = "likes")
public class Like {

    @Id
    @Column(name = "book_id")
    private Long bookId;

    @Id
    @Column(name = "customer_id")
    private Long customerId;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    private Book book;

    @ManyToOne
    @MapsId("customerId")
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Member member;

}
