package com.nhnacademy.heukbaekbookshop.review.controller;

import com.nhnacademy.heukbaekbookshop.image.domain.ReviewImage;
import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import com.nhnacademy.heukbaekbookshop.order.domain.Review;
import com.nhnacademy.heukbaekbookshop.order.exception.PaymentFailureException;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepository;
import com.nhnacademy.heukbaekbookshop.review.ReviewImageConverter;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewCreateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewImageRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewUpdateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.response.ReviewCreateResponse;
import com.nhnacademy.heukbaekbookshop.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.heukbaekbookshop.review.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final OrderRepository orderRepository;

    public ReviewController(ReviewService reviewService, OrderRepository orderRepository) {
        this.reviewService = reviewService;
        this.orderRepository = orderRepository;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<ReviewCreateResponse> createReview(
            @RequestHeader(value = "X-USER-ID") Long customerId,
            @RequestParam("orderId") String orderId,
            @RequestParam("bookId") Long bookId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("score") int score,
            @RequestBody(required = false) List<ReviewImageRequest> images) {
        log.info("들어옴");

        List<MultipartFile> multipartFiles = ReviewImageConverter.convertReviewImageRequestsToMultipartFiles(images);
        Order order = orderRepository.findByTossOrderId(orderId)
                .orElseThrow(() -> new PaymentFailureException("주문 정보를 찾을 수 없습니다."));

        ReviewCreateRequest request = new ReviewCreateRequest(order.getId(), bookId, title, content, score, multipartFiles);
        Review review = reviewService.createReview(customerId, request);

        List<String> imageUrls = new ArrayList<>();
        for (ReviewImage image : review.getReviewImages()) {
            imageUrls.add(image.getUrl());
        }

        ReviewCreateResponse response = new ReviewCreateResponse(review.getTitle(),imageUrls);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{orderId}/{bookId}")
    public ResponseEntity<ReviewDetailResponse> updateReview(
            @RequestHeader(value = "X-USER-ID") Long customerId,
            @PathVariable Long orderId,
            @PathVariable Long bookId,
            @ModelAttribute ReviewUpdateRequest request) {
        ReviewDetailResponse updatedReview = reviewService.updateReview(customerId, orderId, bookId, request);
        return ResponseEntity.ok(updatedReview);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewDetailResponse>> getReviewsByBook(@PathVariable Long bookId) {
        List<ReviewDetailResponse> reviews = reviewService.getReviewsByBook(bookId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReviewDetailResponse>> getMyReviews(@RequestHeader(value = "X-USER-ID") Long customerId) {
        List<ReviewDetailResponse> reviews = reviewService.getReviewsByCustomer(customerId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }


    @DeleteMapping("/{orderId}/{bookId}")
    public void deleteReview(
            @RequestHeader(value = "X-USER-ID") Long customerId,
            @PathVariable Long orderId,
            @PathVariable Long bookId) {
        reviewService.deleteReview(customerId, orderId, bookId);
    }

    @GetMapping("/{orderId}/{bookId}")
    public Boolean getReviewStatus(
            @RequestHeader(value = "X-USER-ID") Long customerId,
            @PathVariable Long orderId,
            @PathVariable Long bookId) {
        return reviewService.hasReview(customerId, orderId, bookId);
    }



    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<ReviewDetailResponse>> getReviewsByOrder(@PathVariable String orderId) {

        Order order = orderRepository.findByTossOrderId(orderId)
                .orElseThrow(() -> new PaymentFailureException("주문 정보를 찾을 수 없습니다."));

        List<ReviewDetailResponse> reviews = reviewService.getReviewsByOrder(order.getId());
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

}
