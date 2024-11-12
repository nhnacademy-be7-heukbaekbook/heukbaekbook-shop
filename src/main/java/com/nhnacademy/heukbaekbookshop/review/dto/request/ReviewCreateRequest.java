package com.nhnacademy.heukbaekbookshop.review.dto.request;

import java.util.List;

/**
 * 리뷰 생성 요청 DTO
 */
public class ReviewCreateRequest {
    private Long customerId;
    private Long orderId;
    private Long bookId;
    private String title;
    private String content;
    private int score;
    private List<String> imageUrls;

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
}
