//package com.nhnacademy.heukbaekbookshop.review.service;
//
//import com.nhnacademy.heukbaekbookshop.common.auth.AuthService;
//import com.nhnacademy.heukbaekbookshop.image.service.ImageUploadService;
//import com.nhnacademy.heukbaekbookshop.order.domain.*;
//import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewCreateRequest;
//import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewUpdateRequest;
//import com.nhnacademy.heukbaekbookshop.review.dto.response.ReviewDetailResponse;
//import com.nhnacademy.heukbaekbookshop.review.repository.ReviewImageRepository;
//import com.nhnacademy.heukbaekbookshop.review.repository.ReviewRepository;
//import com.nhnacademy.heukbaekbookshop.order.repository.OrderBookRepository;
//import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//class ReviewServiceTest {
//
//    @Mock
//    private ReviewRepository reviewRepository;
//
//    @Mock
//    private ReviewImageRepository reviewImageRepository;
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @Mock
//    private OrderBookRepository orderBookRepository;
//
//    @Mock
//    private AuthService authService;
//
//    @Mock
//    private ImageUploadService imageUploadService;
//
//    @InjectMocks
//    private ReviewService reviewService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void createReview_success() {
//        // Given
//        ReviewCreateRequest request = new ReviewCreateRequest(
//                1L, 1L, "Great Book", "Enjoyed it!", 5,
//                List.of(mock(MultipartFile.class), mock(MultipartFile.class))
//        );
//
//        Order order = mock(Order.class);
//        when(order.getOrderBooks()).thenReturn(List.of(new OrderBook(1L)));
//        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
//
//        when(imageUploadService.uploadPhoto(any(MultipartFile.class), anyLong(), anyLong(), anyLong()))
//                .thenReturn("http://image-url.com");
//
//        Review review = new Review();
//        review.setCustomerId(1L);
//        review.setOrderId(1L);
//        review.setBookId(1L);
//        review.setScore(5);
//        review.setTitle("Great Book");
//        review.setContent("Enjoyed it!");
//
//        when(reviewRepository.save(any(Review.class))).thenReturn(review);
//
//        // When
//        ReviewDetailResponse response = reviewService.createReview(1L, request);
//
//        // Then
//        assertNotNull(response);
//        assertEquals("Great Book", response.title());
//        assertEquals("Enjoyed it!", response.content());
//        assertEquals(5, response.score());
//    }
//
//    @Test
//    void updateReview_success() {
//        // Given
//        ReviewUpdateRequest request = new ReviewUpdateRequest("Updated Title", "Updated Content", 4);
//
//        Review review = new Review();
//        review.setCustomerId(1L);
//        review.setOrderId(1L);
//        review.setBookId(1L);
//        review.setScore(5);
//        review.setTitle("Old Title");
//        review.setContent("Old Content");
//
//        when(reviewRepository.findById(any(ReviewPK.class))).thenReturn(Optional.of(review));
//        when(reviewRepository.save(any(Review.class))).thenReturn(review);
//
//        // When
//        ReviewDetailResponse response = reviewService.updateReview(1L, 1L, 1L, request);
//
//        // Then
//        assertNotNull(response);
//        assertEquals("Updated Title", response.title());
//        assertEquals("Updated Content", response.content());
//        assertEquals(4, response.score());
//    }
//
//    @Test
//    void getReviewsByBook_success() {
//        // Given
//        Review review = new Review();
//        review.setCustomerId(1L);
//        review.setOrderId(1L);
//        review.setBookId(1L);
//        review.setScore(5);
//        review.setTitle("Good Review");
//        review.setContent("Amazing Book");
//
//        when(reviewRepository.findAllByBookId(1L)).thenReturn(List.of(review));
//
//        // When
//        List<ReviewDetailResponse> reviews = reviewService.getReviewsByBook(1L);
//
//        // Then
//        assertFalse(reviews.isEmpty());
//        assertEquals(1, reviews.size());
//        assertEquals("Good Review", reviews.get(0).title());
//        assertEquals("Amazing Book", reviews.get(0).content());
//    }
//
//    @Test
//    void createReview_orderNotFound() {
//        // Given
//        ReviewCreateRequest request = new ReviewCreateRequest(1L, 1L, "Great Book", "Enjoyed it!", 5, List.of());
//
//        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // When & Then
//        Exception exception = assertThrows(IllegalArgumentException.class,
//                () -> reviewService.createReview(1L, request));
//        assertEquals("유효하지 않은 주문 ID입니다.", exception.getMessage());
//    }
//
//    @Test
//    void createReview_invalidScore() {
//        // Given
//        ReviewCreateRequest request = new ReviewCreateRequest(1L, 1L, "Great Book", "Enjoyed it!", 6, List.of());
//
//        Order order = mock(Order.class);
//        when(order.getOrderBooks()).thenReturn(List.of(new OrderBook(1L)));
//        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
//
//        // When & Then
//        Exception exception = assertThrows(IllegalArgumentException.class,
//                () -> reviewService.createReview(1L, request));
//        assertEquals("평가 점수는 1~5점 사이여야 합니다.", exception.getMessage());
//    }
//}
