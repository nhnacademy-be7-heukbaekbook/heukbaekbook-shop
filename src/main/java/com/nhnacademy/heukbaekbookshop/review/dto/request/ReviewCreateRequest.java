package com.nhnacademy.heukbaekbookshop.review.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 리뷰 생성 요청 DTO
 */
public record ReviewCreateRequest(
        Long orderId,
        Long bookId,
        String title,
        String content,
        int score,
        List<MultipartFile> images // Base64 인코딩된 이미지 리스트
) {}
