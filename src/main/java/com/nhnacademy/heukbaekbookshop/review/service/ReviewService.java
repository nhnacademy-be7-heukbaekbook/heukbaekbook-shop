package com.nhnacademy.heukbaekbookshop.review.service;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.common.auth.AuthService;
import com.nhnacademy.heukbaekbookshop.image.domain.PhotoType;
import com.nhnacademy.heukbaekbookshop.image.domain.ReviewImage;
import com.nhnacademy.heukbaekbookshop.image.service.ImageUploadService;
import com.nhnacademy.heukbaekbookshop.memberset.customer.domain.Customer;
import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import com.nhnacademy.heukbaekbookshop.order.domain.Review;
import com.nhnacademy.heukbaekbookshop.order.domain.ReviewPK;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderBookRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepository;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewCreateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewUpdateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.heukbaekbookshop.review.repository.ReviewImageRepository;
import com.nhnacademy.heukbaekbookshop.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final OrderRepository orderRepository;
    private final OrderBookRepository orderBookRepository;
    private final CustomerRepository customerRepository;
    private final BookRepository bookRepository;
    private final AuthService authService;
    private final ImageUploadService imageUploadService;

    public ReviewService(ReviewRepository reviewRepository,
                         ReviewImageRepository reviewImageRepository,
                         OrderRepository orderRepository,
                         OrderBookRepository orderBookRepository,
                         AuthService authService,
                         ImageUploadService imageUploadService,
                         CustomerRepository customerRepository,
                         BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewImageRepository = reviewImageRepository;
        this.orderRepository = orderRepository;
        this.orderBookRepository = orderBookRepository;
        this.authService = authService;
        this.imageUploadService = imageUploadService;
        this.customerRepository = customerRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Review createReview(Long customerId, ReviewCreateRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 고객 ID입니다."));
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 주문 ID입니다."));
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 도서 ID입니다."));


        List<String> imageUrls = request.images().stream()
                .map(image -> imageUploadService.uploadPhoto(image, PhotoType.REVIEW))
                .collect(Collectors.toList());


        Review review = Review.createReview(
                customerId,
                request.bookId(),
                request.orderId(),
                request.score(),
                request.title(),
                request.content(),
                imageUrls
        );

        boolean isBookInOrder = order.getOrderBooks().stream()
                .anyMatch(orderBook -> orderBook.getBookId().equals(request.bookId()));

        if (!isBookInOrder) {
            throw new IllegalArgumentException("해당 도서는 이 주문에 포함되지 않았습니다.");
        }

        reviewRepository.save(review);

        return review;
    }

    @Transactional
    public ReviewDetailResponse updateReview(Long customerId, Long orderId, Long bookId,ReviewUpdateRequest request) {
        ReviewPK reviewPK = new ReviewPK(customerId, bookId, orderId);

        Review review = reviewRepository.findById(reviewPK)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        if (!StringUtils.hasText(request.getTitle()) || !StringUtils.hasText(request.getContent())) {
            throw new IllegalArgumentException("제목과 내용은 필수 항목입니다.");
        }

        review.setTitle(request.getTitle());
        review.setContent(request.getContent());
        review.setScore(request.getScore());
        review.setUpdatedAt(LocalDateTime.now());
        reviewRepository.save(review);

        List<String> imageUrls = reviewImageRepository.findAllByReview(review)
                .stream()
                .map(ReviewImage::getUrl)
                .collect(Collectors.toList());

        return convertToResponse(review, imageUrls);
    }

    @Transactional(readOnly = true)
    public List<ReviewDetailResponse> getReviewsByBook(Long bookId) {
        List<Review> reviews = reviewRepository.findAllByBookId(bookId);

        return reviews.stream().map(review -> {
            List<String> imageUrls = reviewImageRepository.findAllByReview(review)
                    .stream()
                    .map(ReviewImage::getUrl)
                    .collect(Collectors.toList());
            return convertToResponse(review, imageUrls);
        }).collect(Collectors.toList());
    }

    private ReviewDetailResponse convertToResponse(Review review, List<String> imageUrls) {
        return new ReviewDetailResponse(
                review.getCustomerId(),
                review.getBookId(),
                review.getOrderId(),
                review.getContent(),
                review.getTitle(),
                review.getScore(),
                imageUrls
        );
    }
}

//
//    @Transactional(readOnly = true)
//    public ReviewDetailResponse getReview(Long reviewId) {
//        Review review = reviewRepository.findById(reviewId)
//                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
//
//        List<String> imageUrls = reviewImageRepository.findAllByReviewId(reviewId)
//                .stream()
//                .map(ReviewImage::getUrl)
//                .collect(Collectors.toList());
//
//        return convertToResponse(review, imageUrls);
//    }
