package com.nhnacademy.heukbaekbookshop.review.dto.request;

import java.util.List;

/**
 * 리뷰 생성 요청 DTO
 */
public record ReviewCreateRequest(
        Long customerId,
        Long orderId,
        Long bookId,
        String content,
        String title,
        int score,
        List<String> base64Images // Base64 인코딩된 이미지 리스트
) {}
