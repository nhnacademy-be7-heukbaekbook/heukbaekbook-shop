package com.nhnacademy.heukbaekbookshop.image.service;

import com.nhnacademy.heukbaekbookshop.image.domain.PhotoType;
import com.nhnacademy.heukbaekbookshop.image.domain.ReviewImage;
import com.nhnacademy.heukbaekbookshop.order.domain.Review;
import com.nhnacademy.heukbaekbookshop.order.domain.ReviewPK;
import com.nhnacademy.heukbaekbookshop.review.repository.ReviewImageRepository;
import com.nhnacademy.heukbaekbookshop.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final ObjectService objectService;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    @Transactional
    public String uploadReviewImage(MultipartFile file, Long customerId, Long bookId, Long orderId) {
        // 복합키로 리뷰 조회
        ReviewPK reviewPK = new ReviewPK(customerId, bookId, orderId);
        Review review = reviewRepository.findById(reviewPK)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        // ObjectService를 사용해 이미지 업로드
        String imageUrl = objectService.uploadPhoto(file, PhotoType.REVIEW);

        // 업로드된 이미지 정보를 데이터베이스에 저장
        ReviewImage reviewImage = new ReviewImage();
        reviewImage.setReview(review);
        reviewImage.setUrl(imageUrl);
        reviewImageRepository.save(reviewImage);

        return imageUrl;
    }
}
