package com.nhnacademy.heukbaekbookshop.review.service;

import com.nhnacademy.heukbaekbookshop.image.domain.Image;
import com.nhnacademy.heukbaekbookshop.image.domain.ReviewImage;
import com.nhnacademy.heukbaekbookshop.order.domain.Review;
import com.nhnacademy.heukbaekbookshop.order.domain.ReviewPK;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewCreateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewUpdateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.heukbaekbookshop.review.repository.ReviewImageRepository;
import com.nhnacademy.heukbaekbookshop.review.repository.ReviewRepository;
import com.nhnacademy.heukbaekbookshop.image.repository.ImageRepository;
import com.nhnacademy.heukbaekbookshop.memberset.customer.domain.Customer;
import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderRepository;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ImageRepository imageRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;

    public ReviewService(ReviewRepository reviewRepository, ReviewImageRepository reviewImageRepository,
                         ImageRepository imageRepository, CustomerRepository customerRepository,
                         OrderRepository orderRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewImageRepository = reviewImageRepository;
        this.imageRepository = imageRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Review createReview(ReviewCreateRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 고객 ID입니다."));
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 주문 ID입니다."));
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 도서 ID입니다."));

        Review review = new Review();
        review.setCustomer(customer);
        review.setOrder(order);
        review.setBook(book);
        review.setTitle(request.getTitle());
        review.setContent(request.getContent());
        review.setScore(request.getScore());
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);

        return review;
    }

    @Transactional
    public Review updateReview(Long customerId, Long bookId, Long orderId, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(new ReviewPK(customerId, bookId, orderId))
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

        if (!StringUtils.hasText(request.getNewTitle()) || !StringUtils.hasText(request.getNewContent())) {
            throw new IllegalArgumentException("제목과 내용은 필수 항목입니다.");
        }

        review.setTitle(request.getNewTitle());
        review.setContent(request.getNewContent());
        review.setScore(request.getNewScore());
        review.setUpdatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    public List<ReviewDetailResponse> getReviewsByBook(Long bookId) {
        return reviewRepository.findByBookId(bookId)
                .stream()
                .map(review -> new ReviewDetailResponse(
                        review.getBookId(),
                        review.getOrderId(),
                        review.getTitle(),
                        review.getContent(),
                        review.getScore(),
                        review.getCreatedAt(),
                        review.getUpdatedAt(),
                        Collections.emptyList() // 이미지 관련 기능은 추후 구현
                ))
                .collect(Collectors.toList());
    }
}