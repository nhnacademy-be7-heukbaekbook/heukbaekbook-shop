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
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Review> createReview(@RequestHeader(value = "X-USER-ID") Long customerId,
            @ModelAttribute ReviewCreateRequest request) {
        Review review = reviewService.createReview(customerId, request);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}/{bookId}")
    public ResponseEntity<ReviewDetailResponse> updateReview(
            @RequestHeader(value = "X-USER-ID") Long customerId,
            @PathVariable Long orderId,
            @PathVariable Long bookId,
            @RequestBody ReviewUpdateRequest request) {
        ReviewDetailResponse updatedReview = reviewService.updateReview(customerId, orderId, bookId, request);
        return ResponseEntity.ok(updatedReview);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewDetailResponse>> getReviewsByBook(@PathVariable Long bookId) {
        List<ReviewDetailResponse> reviews = reviewService.getReviewsByBook(bookId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
}