package com.nhnacademy.heukbaekbookshop.review.service;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.common.auth.AuthService;
import com.nhnacademy.heukbaekbookshop.image.ImageManagerService;
import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import com.nhnacademy.heukbaekbookshop.image.domain.ReviewImage;
import com.nhnacademy.heukbaekbookshop.memberset.customer.domain.Customer;
import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import com.nhnacademy.heukbaekbookshop.order.domain.Review;
import com.nhnacademy.heukbaekbookshop.order.domain.ReviewPK;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderBookRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepository;
import com.nhnacademy.heukbaekbookshop.point.history.domain.PointType;
import com.nhnacademy.heukbaekbookshop.point.history.dto.request.PointHistoryRequest;
import com.nhnacademy.heukbaekbookshop.point.history.service.PointSaveService;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewCreateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewUpdateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.heukbaekbookshop.review.repository.ReviewImageRepository;
import com.nhnacademy.heukbaekbookshop.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
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
    private final ImageManagerService imageManagerService;
    private final PointSaveService pointSaveService;

    public ReviewService(ReviewRepository reviewRepository,
                         ReviewImageRepository reviewImageRepository,
                         OrderRepository orderRepository,
                         OrderBookRepository orderBookRepository,
                         AuthService authService,
                         ImageManagerService imageManagerService,
                         CustomerRepository customerRepository,
                         BookRepository bookRepository,
                         PointSaveService pointSaveService) {
        this.reviewRepository = reviewRepository;
        this.reviewImageRepository = reviewImageRepository;
        this.orderRepository = orderRepository;
        this.orderBookRepository = orderBookRepository;
        this.authService = authService;
        this.imageManagerService = imageManagerService;
        this.customerRepository = customerRepository;
        this.bookRepository = bookRepository;
        this.pointSaveService = pointSaveService;
    }

    @Transactional
    public Review createReview(Long customerId, ReviewCreateRequest request) {
        // 고객, 주문, 도서 유효성 확인
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 고객 ID입니다."));

        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 주문 ID입니다."));

        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 도서 ID입니다."));

        // 리뷰 생성 및 이미지 업로드 처리
        Review review = Review.createReview(
                customerId,
                request.bookId(),
                request.orderId(),
                request.score(),
                request.title(),
                request.content(),
                request.images(), // 업로드할 이미지 리스트
                file -> imageManagerService.uploadPhoto(file, ImageType.REVIEW) // 업로드 함수 전달
        );

        // 리뷰 저장 (연관된 이미지도 함께 저장됨)
        reviewRepository.save(review);

        // 포인트 적립 로직 추가
        int pointToEarn = request.images().isEmpty() ? 200 : 500; // 이미지 유무에 따라 포인트 차등 지급
        saveReviewPoints(customerId, pointToEarn);

        return review;
    }

    @Transactional
    public ReviewDetailResponse updateReview(Long customerId, Long orderId, Long bookId, ReviewUpdateRequest request) {
        ReviewPK reviewPK = new ReviewPK(customerId, bookId, orderId);

        // 기존 리뷰 조회
        Review review = reviewRepository.findById(reviewPK)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        if (!StringUtils.hasText(request.getTitle()) || !StringUtils.hasText(request.getContent())) {
            throw new IllegalArgumentException("제목과 내용은 필수 항목입니다.");
        }

        // 리뷰 정보 업데이트
        review.updateReview(request.getScore(), request.getTitle(), request.getContent());

        // 이미지 업데이트 처리
        processImagesForUpdate(review, request.getUploadedImages());

        // 리뷰 저장
        reviewRepository.save(review);

        List<String> finalImageUrls = review.getReviewImages().stream()
                .map(ReviewImage::getUrl)
                .collect(Collectors.toList());

        return convertToResponse(review, finalImageUrls);
    }

    private void processImagesForUpdate(Review review, List<MultipartFile> newImages) {
        List<String> currentImageUrls = review.getReviewImages().stream()
                .map(ReviewImage::getUrl)
                .collect(Collectors.toList());

        List<String> uploadedImageUrls = newImages.stream()
                .map(image -> imageManagerService.uploadPhoto(image, ImageType.REVIEW))
                .collect(Collectors.toList());

        // 삭제 및 추가 처리 로직
        List<ReviewImage> imagesToDelete = review.getReviewImages().stream()
                .filter(image -> !uploadedImageUrls.contains(image.getUrl()))
                .collect(Collectors.toList());

        imagesToDelete.forEach(image -> {
            reviewImageRepository.delete(image);
            review.getReviewImages().remove(image);
        });

        for (String imageUrl : uploadedImageUrls) {
            if (!currentImageUrls.contains(imageUrl)) {
                ReviewImage newReviewImage = new ReviewImage();
                newReviewImage.setUrl(imageUrl);
                newReviewImage.setReview(review);
                newReviewImage.setType(ImageType.REVIEW);
                reviewImageRepository.save(newReviewImage);
                review.getReviewImages().add(newReviewImage);
            }
        }
    }

    private void saveReviewPoints(Long customerId, int points) {
        PointHistoryRequest pointRequest = new PointHistoryRequest(
                null, // 리뷰 작성 시 주문과 연관된 경우가 아니므로 null
                "리뷰 작성으로 인한 포인트 적립",
                BigDecimal.valueOf(points),
                LocalDateTime.now(),
                PointType.EARNED
        );
        pointSaveService.createPointHistory(customerId, pointRequest);
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

    @Transactional(readOnly = true)
    public List<ReviewDetailResponse> getReviewsByCustomer(Long customerId) {
        List<Review> reviews = reviewRepository.findAllByCustomerId(customerId);

        return reviews.stream().map(review -> {
            List<String> imageUrls = reviewImageRepository.findAllByReview(review)
                    .stream()
                    .map(ReviewImage::getUrl)
                    .collect(Collectors.toList());
            return convertToResponse(review, imageUrls);
        }).collect(Collectors.toList());
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
