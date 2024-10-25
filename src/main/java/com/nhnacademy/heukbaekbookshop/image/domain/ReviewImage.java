package com.nhnacademy.heukbaekbookshop.image.domain;

import com.nhnacademy.heukbaekbookshop.order.domain.Review;
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
@Table(name = "reviews_images")
public class ReviewImage {

    @Id
    @Column(name = "image_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "image_id", insertable = false, updatable = false)
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "customer_id", referencedColumnName = "customer_id"),
            @JoinColumn(name = "book_id", referencedColumnName = "book_id"),
            @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    })
    private Review review;

}
