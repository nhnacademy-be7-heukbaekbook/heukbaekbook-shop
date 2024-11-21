package com.nhnacademy.heukbaekbookshop.review.controller;

import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewCreateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewUpdateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.heukbaekbookshop.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewDetailResponse> createReview(@RequestBody ReviewCreateRequest request) {
        ReviewDetailResponse response = reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDetailResponse> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateRequest request) {
        ReviewDetailResponse response = reviewService.updateReview(reviewId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDetailResponse> getReview(@PathVariable Long reviewId) {
        ReviewDetailResponse response = reviewService.getReview(reviewId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewDetailResponse>> getReviewsByBook(@PathVariable Long bookId) {
        List<ReviewDetailResponse> responses = reviewService.getReviewsByBook(bookId);
        return ResponseEntity.ok(responses);
    }
}
