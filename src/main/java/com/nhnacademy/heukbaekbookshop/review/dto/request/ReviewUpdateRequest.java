package com.nhnacademy.heukbaekbookshop.review.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 리뷰 수정 요청 DTO
 */
@Getter
@Setter
public class ReviewUpdateRequest {
    private String title;
    private String content;
    private int score;
    List<MultipartFile> uploadedImages;
}
