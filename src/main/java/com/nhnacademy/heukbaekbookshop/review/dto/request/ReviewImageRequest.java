package com.nhnacademy.heukbaekbookshop.review.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewImageRequest {
    private String fileName;      // 파일 이름
    private String contentType;   // 파일 MIME 타입
    private String base64Data;    // Base64로 인코딩된 파일 데이터
}