package com.nhnacademy.heukbaekbookshop.review.service;

import com.nhnacademy.heukbaekbookshop.common.auth.AuthService;
import com.nhnacademy.heukbaekbookshop.image.domain.ReviewImage;
import com.nhnacademy.heukbaekbookshop.image.service.ImageUploadService;
import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderStatus;
import com.nhnacademy.heukbaekbookshop.order.domain.Review;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderBookRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepository;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewCreateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewUpdateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.heukbaekbookshop.review.repository.ReviewImageRepository;
import com.nhnacademy.heukbaekbookshop.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final OrderRepository orderRepository;
    private final OrderBookRepository orderBookRepository;
    private final AuthService authService;
    private final ImageUploadService imageUploadService;

    @Value("${nhn.cloud.tenant-id}")
    private String tenantId;

    @Value("${nhn.cloud.api-user-id}")
    private String apiUserId;

    @Value("${nhn.cloud.api-password}")
    private String apiPassword;

    public ReviewService(ReviewRepository reviewRepository,
                         ReviewImageRepository reviewImageRepository,
                         OrderRepository orderRepository,
                         OrderBookRepository orderBookRepository,
                         AuthService authService,
                         ImageUploadService imageUploadService) {
        this.reviewRepository = reviewRepository;
        this.reviewImageRepository = reviewImageRepository;
        this.orderRepository = orderRepository;
        this.orderBookRepository = orderBookRepository;
        this.authService = authService;
        this.imageUploadService = imageUploadService;
    }

    @Transactional
    public ReviewDetailResponse createReview(ReviewCreateRequest request) {
        // 주문 유효성 검증
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new IllegalArgumentException("리뷰는 배송 완료된 주문에 대해서만 작성 가능합니다.");
        }

        // 특정 주문 내 책 확인
        if (orderBookRepository.findByOrderIdAndBookId(request.orderId(), request.bookId()) == null) {
            throw new IllegalArgumentException("주문에 해당 책이 포함되어 있지 않습니다.");
        }

        // 평가 점수 유효성 검증
        if (request.score() < 1 || request.score() > 5) {
            throw new IllegalArgumentException("평가 점수는 1~5점 사이여야 합니다.");
        }

        // 인증 토큰 발급
        String tokenId = authService.requestToken(tenantId, apiUserId, apiPassword);

        // 리뷰 저장
        Review review = new Review();
        review.setCustomerId(request.customerId());
        review.setBookId(request.bookId());
        review.setOrderId(request.orderId());
        review.setContent(request.content());
        review.setScore(request.score());
        review.setTitle(request.title());
        reviewRepository.save(review);

        // 이미지 업로드 및 저장
        List<String> uploadedUrls = imageUploadService.uploadImages(
                tokenId,
                request.base64Images()
        );

        List<ReviewImage> reviewImages = uploadedUrls.stream().map(url -> {
            ReviewImage image = new ReviewImage();
            image.setReview(review);
            image.setUrl(url);
            image.setImageType("DETAIL");
            return image;
        }).collect(Collectors.toList());
        reviewImageRepository.saveAll(reviewImages);

        return convertToResponse(review, uploadedUrls);
    }

    @Transactional
    public ReviewDetailResponse updateReview(Long reviewId, ReviewUpdateRequest request) {
        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        // 리뷰 데이터 수정
        if (request.content() != null) {
            review.setContent(request.content());
        }
        if (request.score() >= 1 && request.score() <= 5) {
            review.setScore(request.score());
        }
        if (request.title() != null) {
            review.setTitle(request.title());
        }
        reviewRepository.save(review);

        // 이미지 URL 추출
        List<String> imageUrls = review.getReviewImages().stream()
                .map(ReviewImage::getUrl)
                .collect(Collectors.toList());

        return convertToResponse(review, imageUrls);
    }

    @Transactional(readOnly = true)
    public ReviewDetailResponse getReview(Long reviewId) {
        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        // 이미지 URL 추출
        List<String> imageUrls = review.getReviewImages().stream()
                .map(ReviewImage::getUrl)
                .collect(Collectors.toList());

        return convertToResponse(review, imageUrls);
    }

    @Transactional(readOnly = true)
    public List<ReviewDetailResponse> getReviewsByBook(Long bookId) {
        // 특정 도서의 리뷰 조회
        List<Review> reviews = reviewRepository.findAllByBookId(bookId);

        return reviews.stream().map(review -> {
            List<String> imageUrls = review.getReviewImages().stream()
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
