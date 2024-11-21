package com.nhnacademy.heukbaekbookshop.review.dto.request;

/**
 * 리뷰 수정 요청 DTO
 */
public record ReviewUpdateRequest(
        String title,
        String content,
        int score
) {}
