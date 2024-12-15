package com.nhnacademy.heukbaekbookshop.review.service;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import com.nhnacademy.heukbaekbookshop.image.domain.ReviewImage;
import com.nhnacademy.heukbaekbookshop.image.service.ImageManagerService;
import com.nhnacademy.heukbaekbookshop.memberset.customer.domain.Customer;
import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.*;
import com.nhnacademy.heukbaekbookshop.point.history.event.ReviewEvent;
import com.nhnacademy.heukbaekbookshop.point.history.repository.PointHistoryRepository;
import com.nhnacademy.heukbaekbookshop.point.history.service.PointSaveService;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewCreateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewUpdateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.heukbaekbookshop.review.repository.ReviewImageRepository;
import com.nhnacademy.heukbaekbookshop.review.repository.ReviewRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewImageRepository reviewImageRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ImageManagerService imageManagerService;
    @Mock
    private PointSaveService pointSaveService;
    @Mock
    private PointHistoryRepository pointHistoryRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Test
    void createReview_Success() {
        // Given
        Long customerId = 1L;
        Long orderId = 1L;
        Long bookId = 1L;

        ReviewCreateRequest request = new ReviewCreateRequest(
                orderId,
                bookId,
                "content",
                "Great book!",
                5,
                List.of(mock(MultipartFile.class))
        );

        Customer customer = Customer.createCustomer("홍길동", "010-1234-5678", "wjdehdgus1234@gmail.com");
        Order order = Order.createOrder(BigDecimal.valueOf(100000), customer.getName(), customer.getPhoneNumber(), customer.getEmail(), "1234", customer, null);
        order.setStatus(OrderStatus.DELIVERED);
        Book book = new Book();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(imageManagerService.uploadPhoto(any(MultipartFile.class), eq(ImageType.REVIEW))).thenReturn("image_url");
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(memberRepository.existsById(customerId)).thenReturn(true);

        // When
        Review createdReview = reviewService.createReview(customerId, request);

        // Then
        assertNotNull(createdReview);
        assertEquals(customerId, createdReview.getCustomerId());
        assertEquals(bookId, createdReview.getBookId());
        assertEquals(orderId, createdReview.getOrderId());
        assertEquals(1, createdReview.getReviewImages().size());
        verify(eventPublisher, times(1)).publishEvent(any(ReviewEvent.class));
    }

    @Test
    void updateReview_Success() {
        // Given
        Long customerId = 1L;
        Long orderId = 1L;
        Long bookId = 1L;

        ReviewUpdateRequest request = new ReviewUpdateRequest(
                "Updated Title",
                "Updated Content",
                4,
                List.of(mock(MultipartFile.class))
        );

        ReviewPK reviewPK = new ReviewPK(customerId, bookId, orderId);
        Review review = new Review();
        review.setCustomerId(customerId);
        review.setBookId(bookId);
        review.setOrderId(orderId);

        when(reviewRepository.findById(reviewPK)).thenReturn(Optional.of(review));
        when(imageManagerService.uploadPhoto(any(MultipartFile.class), eq(ImageType.REVIEW))).thenReturn("new_image_url");

        // When
        ReviewDetailResponse response = reviewService.updateReview(customerId, orderId, bookId, request);

        // Then
        assertNotNull(response);
        assertEquals("Updated Title", response.title());
        assertEquals("Updated Content", response.content());
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void getReviewsByBook_Success() {
        // Given
        Long bookId = 1L;
        Review review1 = new Review();
        review1.setBookId(bookId);
        Review review2 = new Review();
        review2.setBookId(bookId);

        when(reviewRepository.findAllByBookId(bookId)).thenReturn(List.of(review1, review2));
        when(reviewImageRepository.findAllByReview(any(Review.class))).thenReturn(
                List.of(new ReviewImage(), new ReviewImage())
        );

        // When
        List<ReviewDetailResponse> responses = reviewService.getReviewsByBook(bookId);

        // Then
        assertNotNull(responses);
        assertEquals(2, responses.size());
        verify(reviewRepository, times(1)).findAllByBookId(bookId);
    }

    @Test
    void getReviewsByCustomer_Success() {
        // Given
        Long customerId = 1L;
        Review review1 = new Review();
        review1.setCustomerId(customerId);
        Review review2 = new Review();
        review2.setCustomerId(customerId);

        when(reviewRepository.findAllByCustomerId(customerId)).thenReturn(List.of(review1, review2));
        when(reviewImageRepository.findAllByReview(any(Review.class))).thenReturn(
                List.of(new ReviewImage(), new ReviewImage())
        );

        // When
        List<ReviewDetailResponse> responses = reviewService.getReviewsByCustomer(customerId);

        // Then
        assertNotNull(responses);
        assertEquals(2, responses.size());
        verify(reviewRepository, times(1)).findAllByCustomerId(customerId);
    }

    @Test
    void deleteReview_ShouldDeleteReview() {
        // Given
        Long customerId = 1L;
        Long orderId = 10L;
        Long bookId = 100L;

        Review review = mock(Review.class);
        when(reviewRepository.findByOrderIdAndBookIdAndCustomerId(orderId, bookId, customerId))
                .thenReturn(review);

        // When
        reviewService.deleteReview(customerId, orderId, bookId);

        // Then
        verify(reviewRepository, times(1)).findByOrderIdAndBookIdAndCustomerId(orderId, bookId, customerId);
        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    void hasReview_ShouldReturnTrueIfReviewExists() {
        // Given
        Long customerId = 1L;
        Long orderId = 10L;
        Long bookId = 100L;

        Review review = mock(Review.class);
        when(reviewRepository.findByOrderIdAndBookIdAndCustomerId(orderId, bookId, customerId))
                .thenReturn(review);

        // When
        boolean result = reviewService.hasReview(customerId, orderId, bookId);

        // Then
        assertTrue(result);
        verify(reviewRepository, times(1)).findByOrderIdAndBookIdAndCustomerId(orderId, bookId, customerId);
    }

    @Test
    void hasReview_ShouldReturnFalseIfReviewNotFound() {
        // Given
        Long customerId = 1L;
        Long orderId = 10L;
        Long bookId = 100L;

        when(reviewRepository.findByOrderIdAndBookIdAndCustomerId(orderId, bookId, customerId))
                .thenReturn(null);

        // When
        boolean result = reviewService.hasReview(customerId, orderId, bookId);

        // Then
        assertFalse(result);
        verify(reviewRepository, times(1)).findByOrderIdAndBookIdAndCustomerId(orderId, bookId, customerId);
    }

    @Test
    void getReviewsByOrder_ShouldReturnListOfReviewDetailResponses() {
        // Given
        Long orderId = 10L;

        Review review1 = mock(Review.class);
        Review review2 = mock(Review.class);

        when(reviewRepository.findAllByOrderId(orderId)).thenReturn(Arrays.asList(review1, review2));

        ReviewImage image1 = new ReviewImage(review1);
        ReviewImage image2 = new ReviewImage(review2);
        when(reviewImageRepository.findAllByReview(review1)).thenReturn(Collections.singletonList(image1));
        when(reviewImageRepository.findAllByReview(review2)).thenReturn(Collections.singletonList(image2));

        // When
        List<ReviewDetailResponse> responses = reviewService.getReviewsByOrder(orderId);

        // Then
        assertNotNull(responses);
        assertEquals(2, responses.size());
        verify(reviewRepository, times(1)).findAllByOrderId(orderId);
        verify(reviewImageRepository, times(1)).findAllByReview(review1);
        verify(reviewImageRepository, times(1)).findAllByReview(review2);
    }
}