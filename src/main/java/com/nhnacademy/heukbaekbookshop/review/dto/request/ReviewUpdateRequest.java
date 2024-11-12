package com.nhnacademy.heukbaekbookshop.review.dto.request;

/**
 * 리뷰 수정 요청 DTO
 */
public class ReviewUpdateRequest {
    private String newTitle;
    private String newContent;
    private int newScore;

    public String getNewTitle() { return newTitle; }
    public void setNewTitle(String newTitle) { this.newTitle = newTitle; }

    public String getNewContent() { return newContent; }
    public void setNewContent(String newContent) { this.newContent = newContent; }

    public int getNewScore() { return newScore; }
    public void setNewScore(int newScore) { this.newScore = newScore; }
}
