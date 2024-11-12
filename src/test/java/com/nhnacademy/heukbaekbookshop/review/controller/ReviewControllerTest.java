package com.nhnacademy.heukbaekbookshop.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekbookshop.order.domain.Review;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewCreateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewUpdateRequest;
import com.nhnacademy.heukbaekbookshop.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.heukbaekbookshop.review.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    @Test
    void createReview() throws Exception {
        // Given
        ReviewCreateRequest request = new ReviewCreateRequest();
        request.setCustomerId(1L);
        request.setOrderId(1L);
        request.setBookId(1L);
        request.setTitle("Book1");
        request.setContent("Good");
        request.setScore(5);

        Review review = new Review();
        review.setCustomerId(1L);
        review.setOrderId(1L);
        review.setBookId(1L);
        review.setTitle("Book1");
        review.setContent("Good");
        review.setScore(5);
        review.setCreatedAt(LocalDateTime.now());

        when(reviewService.createReview(any(ReviewCreateRequest.class))).thenReturn(review);

        // When & Then
        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Book1")))
                .andExpect(jsonPath("$.content", is("Good")))
                .andExpect(jsonPath("$.score", is(5)));
    }

    @Test
    void updateReview() throws Exception {
        // Given
        ReviewUpdateRequest request = new ReviewUpdateRequest();
        request.setNewTitle("Updated Title");
        request.setNewContent("Updated Content");
        request.setNewScore(4);

        Review updatedReview = new Review();
        updatedReview.setCustomerId(1L);
        updatedReview.setBookId(1L);
        updatedReview.setOrderId(1L);
        updatedReview.setTitle("Updated Title");
        updatedReview.setContent("Updated Content");
        updatedReview.setScore(4);
        updatedReview.setUpdatedAt(LocalDateTime.now());

        when(reviewService.updateReview(anyLong(), anyLong(), anyLong(), any(ReviewUpdateRequest.class)))
                .thenReturn(updatedReview);

        // When & Then
        mockMvc.perform(put("/api/reviews/1/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Title")))
                .andExpect(jsonPath("$.content", is("Updated Content")))
                .andExpect(jsonPath("$.score", is(4)));
    }

    @Test
    void getReviewsByBook() throws Exception {
        // Given
        ReviewDetailResponse response = new ReviewDetailResponse(
                1L, 1L, "Review", "Content", 5, LocalDateTime.now(), LocalDateTime.now(), Collections.emptyList()
        );

        when(reviewService.getReviewsByBook(anyLong())).thenReturn(List.of(response));

        // When & Then
        mockMvc.perform(get("/api/reviews/book/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Review")))
                .andExpect(jsonPath("$[0].content", is("Content")))
                .andExpect(jsonPath("$[0].score", is(5)));
    }
}
