package com.nhnacademy.heukbaekbookshop.contributor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekbookshop.contributor.dto.request.PublisherCreateRequest;
import com.nhnacademy.heukbaekbookshop.contributor.dto.request.PublisherUpdateRequest;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherCreateResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherDeleteResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherDetailResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherUpdateResponse;
import com.nhnacademy.heukbaekbookshop.contributor.service.PublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublisherController.class)
class PublisherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublisherService publisherService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterPublisher() throws Exception {
        // Given
        PublisherCreateRequest request = new PublisherCreateRequest("Publisher Name");
        PublisherCreateResponse mockResponse = new PublisherCreateResponse("Publisher Name");

        when(publisherService.registerPublisher(any(PublisherCreateRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/admin/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.name").value("Publisher Name")); // 출판사 이름 확인

        verify(publisherService, times(1)).registerPublisher(any(PublisherCreateRequest.class)); // 서비스 호출 검증
    }

    @Test
    void testGetPublishers() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<PublisherDetailResponse> publishers = List.of(
                new PublisherDetailResponse(1L, "Publisher1"),
                new PublisherDetailResponse(2L, "Publisher2")
        );

        Page<PublisherDetailResponse> mockPage = new PageImpl<>(publishers, pageable, publishers.size());

        when(publisherService.getPublishers(any(Pageable.class))).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/api/admin/publishers")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.content[0].id").value(1L)) // 첫 번째 출판사 ID 확인
                .andExpect(jsonPath("$.content[0].name").value("Publisher1")) // 첫 번째 출판사 이름 확인
                .andExpect(jsonPath("$.content[1].id").value(2L)) // 두 번째 출판사 ID 확인
                .andExpect(jsonPath("$.content[1].name").value("Publisher2")); // 두 번째 출판사 이름 확인

        verify(publisherService, times(1)).getPublishers(any(Pageable.class)); // 서비스 호출 검증
    }

    @Test
    void testGetPublisherById() throws Exception {
        // Given
        Long publisherId = 1L;
        PublisherDetailResponse mockResponse = new PublisherDetailResponse(publisherId, "Publisher Name");

        when(publisherService.getPublisherById(anyLong())).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(get("/api/admin/publishers/{publisher-id}", publisherId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.id").value(1L)) // 출판사 ID 확인
                .andExpect(jsonPath("$.name").value("Publisher Name")); // 출판사 이름 확인

        verify(publisherService, times(1)).getPublisherById(anyLong()); // 서비스 호출 검증
    }

    @Test
    void testUpdatePublisher() throws Exception {
        // Given
        Long publisherId = 1L;
        PublisherUpdateRequest request = new PublisherUpdateRequest("Updated Publisher Name");
        PublisherUpdateResponse mockResponse = new PublisherUpdateResponse("Updated Publisher Name");

        when(publisherService.updatePublisher(anyLong(), any(PublisherUpdateRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(put("/api/admin/publishers/{publisher-id}", publisherId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.name").value("Updated Publisher Name")); // 업데이트된 출판사 이름 확인

        verify(publisherService, times(1)).updatePublisher(anyLong(), any(PublisherUpdateRequest.class)); // 서비스 호출 검증
    }
    
    @Test
    void testDeletePublisher() throws Exception {
        // Given
        Long publisherId = 1L;
        PublisherDeleteResponse mockResponse = new PublisherDeleteResponse("Deleted Publisher Name");

        when(publisherService.deletePublisher(anyLong())).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(delete("/api/admin/publishers/{publisher-id}", publisherId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.name").value("Deleted Publisher Name")); // 삭제된 출판사 이름 확인

        verify(publisherService, times(1)).deletePublisher(anyLong()); // 서비스 호출 검증
    }

}
