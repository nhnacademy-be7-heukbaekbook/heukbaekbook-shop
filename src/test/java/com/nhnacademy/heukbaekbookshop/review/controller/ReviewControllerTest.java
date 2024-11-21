package com.nhnacademy.heukbaekbookshop.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewCreateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewUpdateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.heukbaekbookshop.review.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

    @Test
    @DisplayName("POST /reviews - 성공")
    void createReview_success() throws Exception {
        // Given
        ReviewCreateRequest request = new ReviewCreateRequest(
                1L, // orderId
                2L, // bookId
                3L, // customerId
                "Amazing book!",
                "This book is fantastic.",
                5,
                List.of("base64Image1", "base64Image2")
        );

        ReviewDetailResponse response = new ReviewDetailResponse(
                3L,
                2L,
                1L,
                "This book is fantastic.",
                "Amazing book!",
                5,
                List.of("uploadedImage1", "uploadedImage2")
        );

        Mockito.when(reviewService.createReview(any(ReviewCreateRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(3))
                .andExpect(jsonPath("$.bookId").value(2))
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.content").value("This book is fantastic."))
                .andExpect(jsonPath("$.title").value("Amazing book!"))
                .andExpect(jsonPath("$.score").value(5))
                .andExpect(jsonPath("$.imageUrls[0]").value("uploadedImage1"))
                .andExpect(jsonPath("$.imageUrls[1]").value("uploadedImage2"));
    }

    @Test
    @DisplayName("POST /reviews - 실패: 잘못된 요청")
    void createReview_failure_invalidRequest() throws Exception {
        // Given
        ReviewCreateRequest invalidRequest = new ReviewCreateRequest(
                null, // orderId
                null, // bookId
                null, // customerId
                null,
                null,
                0,
                null
        );

        // When & Then
        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /reviews/{reviewId} - 성공")
    void getReview_success() throws Exception {
        // Given
        ReviewDetailResponse response = new ReviewDetailResponse(
                3L,
                2L,
                1L,
                "This book is fantastic.",
                "Amazing book!",
                5,
                List.of("uploadedImage1", "uploadedImage2")
        );

        Mockito.when(reviewService.getReview(anyLong())).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/reviews/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(3))
                .andExpect(jsonPath("$.bookId").value(2))
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.content").value("This book is fantastic."))
                .andExpect(jsonPath("$.title").value("Amazing book!"))
                .andExpect(jsonPath("$.score").value(5))
                .andExpect(jsonPath("$.imageUrls[0]").value("uploadedImage1"))
                .andExpect(jsonPath("$.imageUrls[1]").value("uploadedImage2"));
    }

    @Test
    @DisplayName("GET /reviews/book/{bookId} - 성공")
    void getReviewsByBook_success() throws Exception {
        // Given
        List<ReviewDetailResponse> responses = List.of(
                new ReviewDetailResponse(3L, 2L, 1L, "Content 1", "Title 1", 4, List.of("image1")),
                new ReviewDetailResponse(3L, 2L, 1L, "Content 2", "Title 2", 5, List.of("image2"))
        );

        Mockito.when(reviewService.getReviewsByBook(anyLong())).thenReturn(responses);

        // When & Then
        mockMvc.perform(get("/reviews/book/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title 1"))
                .andExpect(jsonPath("$[0].content").value("Content 1"))
                .andExpect(jsonPath("$[1].title").value("Title 2"))
                .andExpect(jsonPath("$[1].content").value("Content 2"));
    }
}
