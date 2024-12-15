package com.nhnacademy.heukbaekbookshop.order.domain;

import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import com.nhnacademy.heukbaekbookshop.image.domain.ReviewImage;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
    private Long customerId;

    @Id
    @Column(name = "book_id")
    private Long bookId;

    @Id
    @Column(name = "order_id")
    private Long orderId;
//
//    @ManyToOne
//    @MapsId("customerId")
//    @JoinColumn(name = "customer_id")
//    private Customer customer;
//
//    @ManyToOne
//    @MapsId("bookId")
//    @JoinColumn(name = "book_id")
//    private Book book;
//
//    @ManyToOne
//    @MapsId("orderId")
//    @JoinColumn(name = "order_id")
//    private Order order;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImages = new ArrayList<>();

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
    @Column(name = "review_title")
    private String title;

    @NotNull
    @Column(name = "review_content")
    private String content;


    public static Review createReview(Long customerId, Long bookId, Long orderId, int reviewScore, String reviewTitle, String reviewContent, List<MultipartFile> reviewImages, Function<MultipartFile, String> uploadFunction
    ) {
        Review review = new Review();
        review.customerId = customerId;
        review.bookId = bookId;
        review.orderId = orderId;
        review.score = reviewScore;
        review.title = reviewTitle;
        review.content = reviewContent;
        review.createdAt = LocalDateTime.now();
        for (MultipartFile file : reviewImages) {
            String imageUrl = uploadFunction.apply(file);
            ReviewImage reviewImage = new ReviewImage();
            reviewImage.setUrl(imageUrl);
            reviewImage.setReview(review);
            reviewImage.setType(ImageType.REVIEW);
            review.reviewImages.add(reviewImage);
        }        return review;
    }

    public void updateReview(int reviewScore, String reviewTitle, String reviewContent) {
        this.score = reviewScore;
        this.title = reviewTitle;
        this.content = reviewContent;
        this.updatedAt = LocalDateTime.now();
    }
}
