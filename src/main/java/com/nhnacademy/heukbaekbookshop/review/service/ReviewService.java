package com.nhnacademy.heukbaekbookshop.review.service;

import com.nhnacademy.heukbaekbookshop.common.auth.AuthService;
import com.nhnacademy.heukbaekbookshop.image.domain.ReviewImage;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewCreateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewUpdateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.heukbaekbookshop.review.repository.ReviewImageRepository;
import com.nhnacademy.heukbaekbookshop.review.repository.ReviewRepository;
import com.nhnacademy.heukbaekbookshop.image.service.ImageUploadService;
import com.nhnacademy.heukbaekbookshop.order.domain.Review;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
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
                         AuthService authService,
                         ImageUploadService imageUploadService) {
        this.reviewRepository = reviewRepository;
        this.reviewImageRepository = reviewImageRepository;
        this.authService = authService;
        this.imageUploadService = imageUploadService;
    }

    @Transactional
    public ReviewDetailResponse createReview(ReviewCreateRequest request) {
        // 인증 토큰 발급
        String tokenId = authService.requestToken(tenantId, apiUserId, apiPassword);

        // 리뷰 데이터 저장
        Review review = new Review();
        review.setCustomerId(request.customerId());
        review.setBookId(request.bookId());
        review.setOrderId(request.orderId());
        review.setContent(request.content());
        review.setScore(request.score());
        review.setTitle(request.title());
        reviewRepository.save(review);

        // 이미지 업로드 및 저장
        List<String> uploadedUrls = imageUploadService.uploadImages(tokenId, request.base64Images());
        List<ReviewImage> reviewImages = uploadedUrls.stream().map(url -> {
            ReviewImage image = new ReviewImage();
            image.setReview(review);
            image.setUrl(url);
            image.setImageType("DETAIL");
            return image;
        }).collect(Collectors.toList());
        reviewImageRepository.saveAll(reviewImages);

        // 응답 데이터 생성
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
        if (request.score() > 0) {
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

        // 응답 데이터 생성
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

        // 응답 데이터 생성
        return convertToResponse(review, imageUrls);
    }

    @Transactional(readOnly = true)
    public List<ReviewDetailResponse> getReviewsByBook(Long bookId) {
        // 특정 도서의 리뷰 조회
        List<Review> reviews = reviewRepository.findAllByBookId(bookId);

        // 리뷰 목록 변환
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
                review.getScore(),
                review.getTitle(),
                imageUrls
        );
    }
}
