package com.nhnacademy.heukbaekbookshop.review.dto.response;

import java.util.List;

/**
 * 리뷰 상세 조회 응답 DTO
 */
public record ReviewDetailResponse(
        Long customerId,
        Long bookId,
        Long orderId,
        String content,
        String title,
        int score,
        List<String> imageUrls // 업로드된 이미지 URL 리스트
) {}
