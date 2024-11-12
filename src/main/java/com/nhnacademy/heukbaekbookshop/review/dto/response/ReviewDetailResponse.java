package com.nhnacademy.heukbaekbookshop.review.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 리뷰 상세 조회 응답 DTO
 */
public class ReviewDetailResponse {
    private Long bookId;
    private Long orderId;
    private String title;
    private String content;
    private int score;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> imageUrls;

    public ReviewDetailResponse(Long bookId, Long orderId, String title, String content, int score, LocalDateTime createdAt, LocalDateTime updatedAt, List<String> imageUrls) {
        this.bookId = bookId;
        this.orderId = orderId;
        this.title = title;
        this.content = content;
        this.score = score;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters only
    public Long getBookId() { return bookId; }
    public Long getOrderId() { return orderId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public int getScore() { return score; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<String> getImageUrls() { return imageUrls; }
}
