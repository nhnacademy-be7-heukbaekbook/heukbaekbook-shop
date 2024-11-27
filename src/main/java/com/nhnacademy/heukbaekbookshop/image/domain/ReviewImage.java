package com.nhnacademy.heukbaekbookshop.image.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class ReviewImage extends Image {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", nullable = false),
            @JoinColumn(name = "book_id", referencedColumnName = "book_id", nullable = false),
            @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
    })

    @JsonIgnore
    private Review review;
}
