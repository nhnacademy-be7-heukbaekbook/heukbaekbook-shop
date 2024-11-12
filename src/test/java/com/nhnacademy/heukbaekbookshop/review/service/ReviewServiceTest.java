package com.nhnacademy.heukbaekbookshop.review.service;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.memberset.customer.domain.Customer;
import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import com.nhnacademy.heukbaekbookshop.order.domain.Review;
import com.nhnacademy.heukbaekbookshop.order.domain.ReviewPK;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderRepository;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewCreateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewUpdateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.heukbaekbookshop.review.repository.ReviewRepository;
import com.nhnacademy.heukbaekbookshop.image.repository.ImageRepository;
import com.nhnacademy.heukbaekbookshop.review.repository.ReviewImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewImageRepository reviewImageRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReview() {
        // Given
        ReviewCreateRequest request = new ReviewCreateRequest();
        request.setCustomerId(1L);
        request.setOrderId(1L);
        request.setBookId(1L);
        request.setTitle("Book1");
        request.setContent("Good");
        request.setScore(5);

        Customer customer = Customer.createCustomer("name", "010-1234-5678", "abc@gmail.com");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Book book = new Book();
        book.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Review review = new Review();
        review.setCustomer(customer);
        review.setOrder(order);
        review.setBook(book);
        review.setTitle(request.getTitle());
        review.setContent(request.getContent());
        review.setScore(request.getScore());
        review.setCreatedAt(LocalDateTime.now());

        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // When
        Review createdReview = reviewService.createReview(request);

        // Then
        assertNotNull(createdReview);
        assertEquals("Book1", createdReview.getTitle());
        assertEquals("Good", createdReview.getContent());
        assertEquals(5, createdReview.getScore());
        assertEquals(customer, createdReview.getCustomer());
        assertEquals(order, createdReview.getOrder());
        assertEquals(book, createdReview.getBook());
    }

    @Test
    void updateReview() {
        // Given
        Long customerId = 1L;
        Long bookId = 1L;
        Long orderId = 1L;
        ReviewUpdateRequest request = new ReviewUpdateRequest();
        request.setNewTitle("Updated Title");
        request.setNewContent("Updated Content");
        request.setNewScore(4);

        Review review = new Review();
        review.setCustomerId(customerId);
        review.setBookId(bookId);
        review.setOrderId(orderId);
        review.setTitle("Old Title");
        review.setContent("Old Content");
        review.setScore(3);

        when(reviewRepository.findById(new ReviewPK(customerId, bookId, orderId))).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // When
        Review updatedReview = reviewService.updateReview(customerId, bookId, orderId, request);

        // Then
        assertNotNull(updatedReview);
        assertEquals("Updated Title", updatedReview.getTitle());
        assertEquals("Updated Content", updatedReview.getContent());
        assertEquals(4, updatedReview.getScore());
    }

    @Test
    void getReviewsByBook() {
        // Given
        Long bookId = 1L;
        Review review = new Review();
        review.setBookId(bookId);
        review.setTitle("Review");
        review.setContent("Content");
        review.setScore(5);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        when(reviewRepository.findByBookId(bookId)).thenReturn(List.of(review));

        // When
        List<ReviewDetailResponse> reviews = reviewService.getReviewsByBook(bookId);

        // Then
        assertNotNull(reviews);
        assertFalse(reviews.isEmpty());
        assertEquals(1, reviews.size());
        assertEquals("Review", reviews.get(0).getTitle());
        assertEquals("Content", reviews.get(0).getContent());
        assertEquals(5, reviews.get(0).getScore());
    }
}
