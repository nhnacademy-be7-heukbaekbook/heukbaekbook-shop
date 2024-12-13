package com.nhnacademy.heukbaekbookshop.review.controller;

import com.nhnacademy.heukbaekbookshop.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.heukbaekbookshop.review.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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