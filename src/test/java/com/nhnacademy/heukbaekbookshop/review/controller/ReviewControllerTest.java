//package com.nhnacademy.heukbaekbookshop.review.controller;
//
//import com.nhnacademy.heukbaekbookshop.order.domain.Review;
//import com.nhnacademy.heukbaekbookshop.review.controller.ReviewController;
//import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewCreateRequest;
//import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewUpdateRequest;
//import com.nhnacademy.heukbaekbookshop.review.dto.response.ReviewDetailResponse;
//import com.nhnacademy.heukbaekbookshop.review.service.ReviewService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//class ReviewControllerTest {
//
//    @Mock
//    private ReviewService reviewService;
//
//    @InjectMocks
//    private ReviewController reviewController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCreateReview() {
//        // Given
//        Long customerId = 1L;
//        ReviewCreateRequest request = new ReviewCreateRequest(1L, 1L, "Content", "Title", 5, List.of());
//        Review mockReview = new Review();
//        mockReview.setCustomerId(customerId);
//
//        when(reviewService.createReview(anyLong(), any(ReviewCreateRequest.class))).thenReturn(mockReview);
//
//        // When
//        ResponseEntity<Review> response = reviewController.createReview(customerId, request);
//
//        // Then
//        assertEquals(201, response.getStatusCodeValue());
//        assertEquals(mockReview, response.getBody());
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
//
//    @Test
//    void testGetReviewsByBook() {
//        // Given
//        Long bookId = 1L;
//        List<ReviewDetailResponse> mockResponse = List.of(
//                new ReviewDetailResponse(1L, bookId, 1L, "Content", "Title", 5, List.of())
//        );
//
//        when(reviewService.getReviewsByBook(anyLong())).thenReturn(mockResponse);
//
//        // When
//        ResponseEntity<List<ReviewDetailResponse>> response = reviewController.getReviewsByBook(bookId);
//
//        // Then
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(mockResponse, response.getBody());
//    }
//
//    @Test
//    void testGetMyReviews() {
//        // Given
//        Long customerId = 1L;
//        List<ReviewDetailResponse> mockResponse = List.of(
//                new ReviewDetailResponse(customerId, 1L, 1L, "Content", "Title", 5, List.of())
//        );
//
//        when(reviewService.getReviewsByCustomer(anyLong())).thenReturn(mockResponse);
//
//        // When
//        ResponseEntity<List<ReviewDetailResponse>> response = reviewController.getMyReviews(customerId);
//
//        // Then
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(mockResponse, response.getBody());
//    }
//}
