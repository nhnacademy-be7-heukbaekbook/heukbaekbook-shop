package com.nhnacademy.heukbaekbookshop.review.controller;

import com.nhnacademy.heukbaekbookshop.image.domain.ReviewImage;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewCreateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewImageRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.response.ReviewCreateResponse;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewUpdateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.heukbaekbookshop.review.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testCreateReview() {
//        // Given
//        Long customerId = 1L;
//        String orderId = "test-order-id";
//        Long bookId = 1L;
//        String title = "Review Title";
//        String content = "Review Content";
//        int score = 5;
//
//        // Mocked Image Requests
//        List<ReviewImageRequest> imageRequests = List.of(
//                new ReviewImageRequest("file1.jpg", "image/jpeg", "base64Data1"),
//                new ReviewImageRequest("file2.png", "image/png", "base64Data2")
//        );
//
//        ReviewCreateResponse mockResponse = new ReviewCreateResponse("Review Title", List.of("image-url-1", "image-url-2"));
//
//        when(reviewService.createReview(anyLong(), any(ReviewCreateRequest.class)));
//
//        // When
//        ResponseEntity<ReviewCreateResponse> response = reviewController.createReview(
//                customerId, orderId, bookId, title, content, score, imageRequests);
//
//        // Then
//        assertEquals(201, response.getStatusCodeValue());
//        assertEquals(mockResponse, response.getBody());
//    }
//
//    @Test
//    void testUpdateReview() {
//        // Given
//        Long customerId = 1L;
//        Long orderId = 1L;
//        Long bookId = 1L;
//        ReviewUpdateRequest request = new ReviewUpdateRequest();
//        request.setTitle("Updated Title");
//        request.setContent("Updated Content");
//        request.setScore(4);
//
//        ReviewDetailResponse mockResponse = new ReviewDetailResponse(
//                customerId, bookId, orderId, "Updated Content", "Updated Title", 4, List.of());
//
//        when(reviewService.updateReview(anyLong(), anyLong(), anyLong(), any(ReviewUpdateRequest.class)))
//                .thenReturn(mockResponse);
//
//        // When
//        ResponseEntity<ReviewDetailResponse> response = reviewController.updateReview(customerId, orderId, bookId, request);
//
//        // Then
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(mockResponse, response.getBody());
//    }

    @Test
    void testGetReviewsByBook() {
        // Given
        Long bookId = 1L;
        List<ReviewDetailResponse> mockResponse = List.of(
                new ReviewDetailResponse(1L, bookId, 1L, "Content", "Title", 5, List.of())
        );

        when(reviewService.getReviewsByBook(anyLong())).thenReturn(mockResponse);

        // When
        ResponseEntity<List<ReviewDetailResponse>> response = reviewController.getReviewsByBook(bookId);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testGetMyReviews() {
        // Given
        Long customerId = 1L;
        List<ReviewDetailResponse> mockResponse = List.of(
                new ReviewDetailResponse(customerId, 1L, 1L, "Content", "Title", 5, List.of())
        );

        when(reviewService.getReviewsByCustomer(anyLong())).thenReturn(mockResponse);

        // When
        ResponseEntity<List<ReviewDetailResponse>> response = reviewController.getMyReviews(customerId);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testDeleteReview() {
        // Given
        Long customerId = 1L;
        Long orderId = 1L;
        Long bookId = 1L;

        // When
        reviewController.deleteReview(customerId, orderId, bookId);

        // Then
        // No exception should be thrown
    }

    @Test
    void testGetReviewStatus() {
        // Given
        Long customerId = 1L;
        Long orderId = 1L;
        Long bookId = 1L;
        when(reviewService.hasReview(anyLong(), anyLong(), anyLong())).thenReturn(true);

        // When
        Boolean status = reviewController.getReviewStatus(customerId, orderId, bookId);

        // Then
        assertEquals(true, status);
    }
}