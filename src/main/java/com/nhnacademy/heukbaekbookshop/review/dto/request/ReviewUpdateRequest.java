package com.nhnacademy.heukbaekbookshop.review.dto.request;

/**
 * 리뷰 수정 요청 DTO
 */
public record ReviewUpdateRequest(
        String content,
        int score,
        String title
) {}
