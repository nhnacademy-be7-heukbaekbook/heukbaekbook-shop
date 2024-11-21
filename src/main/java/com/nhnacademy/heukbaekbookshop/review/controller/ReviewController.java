package com.nhnacademy.heukbaekbookshop.review.controller;

import com.nhnacademy.heukbaekbookshop.order.domain.Review;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewCreateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewUpdateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.heukbaekbookshop.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {fg
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewCreateRequest request) {
        Review review = reviewService.createReview(request);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @PutMapping("/{customerId}/{bookId}/{orderId}")
    public ResponseEntity<Review> updateReview(
            @PathVariable Long customerId,
            @PathVariable Long bookId,
            @PathVariable Long orderId,
            @RequestBody ReviewUpdateRequest request) {
        Review updatedReview = reviewService.updateReview(customerId, bookId, orderId, request);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewDetailResponse>> getReviewsByBook(@PathVariable Long bookId) {
        List<ReviewDetailResponse> reviews = reviewService.getReviewsByBook(bookId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
}
