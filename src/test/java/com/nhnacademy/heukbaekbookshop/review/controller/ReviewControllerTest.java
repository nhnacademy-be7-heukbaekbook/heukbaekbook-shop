//package com.nhnacademy.heukbaekbookshop.review.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewCreateRequest;
//import com.nhnacademy.heukbaekbookshop.review.dto.response.ReviewDetailResponse;
//import com.nhnacademy.heukbaekbookshop.review.service.ReviewService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ReviewController.class)
//class ReviewControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private ReviewService reviewService;
//
//    @Test
//    @DisplayName("POST /reviews - 성공")
//    void createReview_success() throws Exception {
//        // Given
//        ReviewCreateRequest request = new ReviewCreateRequest(
//                1L, 1L, 1L, "Great Book", "Amazing content!", 5, List.of("image1", "image2")
//        );
//
//        ReviewDetailResponse response = new ReviewDetailResponse(
//                1L, 1L, 1L, "Amazing content!", "Great Book", 5, List.of("http://mock-image-url")
//        );
//
//        Mockito.when(reviewService.createReview(anyLong(), any(ReviewCreateRequest.class))).thenReturn(response);
//
//        // When & Then
//        mockMvc.perform(post("/api/reviews")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("X-USER-ID", "1")
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.customerId").value(1))
//                .andExpect(jsonPath("$.bookId").value(1))
//                .andExpect(jsonPath("$.orderId").value(1))
//                .andExpect(jsonPath("$.title").value("Great Book"))
//                .andExpect(jsonPath("$.content").value("Amazing content!"))
//                .andExpect(jsonPath("$.imageUrls[0]").value("http://mock-image-url"));
//    }
//}
