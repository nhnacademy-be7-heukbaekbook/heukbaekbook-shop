package com.nhnacademy.heukbaekbookshop.contributor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekbookshop.contributor.dto.request.ContributorCreateRequest;
import com.nhnacademy.heukbaekbookshop.contributor.dto.request.ContributorUpdateRequest;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorCreateResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorDeleteResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorDetailResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorUpdateResponse;
import com.nhnacademy.heukbaekbookshop.contributor.service.ContributorService;
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

@WebMvcTest(ContributorController.class)
class ContributorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContributorService contributorService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterContributor() throws Exception {
        // Given
        ContributorCreateRequest request = new ContributorCreateRequest("Contributor Name", "Contributor Description");
        ContributorCreateResponse mockResponse = new ContributorCreateResponse("Contributor Name", "Contributor Description");

        when(contributorService.registerContributor(any(ContributorCreateRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/admin/contributors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.name").value("Contributor Name")) // 이름 확인
                .andExpect(jsonPath("$.description").value("Contributor Description")); // 설명 확인

        verify(contributorService, times(1)).registerContributor(any(ContributorCreateRequest.class)); // 서비스 호출 검증
    }

    @Test
    void testGetContributors() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<ContributorDetailResponse> contributors = List.of(
                new ContributorDetailResponse(1L, "Contributor1", "Description1"),
                new ContributorDetailResponse(2L, "Contributor2", "Description2")
        );

        Page<ContributorDetailResponse> mockPage = new PageImpl<>(contributors, pageable, contributors.size());

        when(contributorService.getContributors(any(Pageable.class))).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/api/admin/contributors")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.content[0].id").value(1L)) // 첫 번째 기여자 ID 확인
                .andExpect(jsonPath("$.content[0].name").value("Contributor1")) // 첫 번째 기여자 이름 확인
                .andExpect(jsonPath("$.content[0].description").value("Description1")) // 첫 번째 기여자 설명 확인
                .andExpect(jsonPath("$.content[1].id").value(2L)) // 두 번째 기여자 ID 확인
                .andExpect(jsonPath("$.content[1].name").value("Contributor2")) // 두 번째 기여자 이름 확인
                .andExpect(jsonPath("$.content[1].description").value("Description2")); // 두 번째 기여자 설명 확인

        verify(contributorService, times(1)).getContributors(any(Pageable.class)); // 서비스 호출 검증
    }

    @Test
    void testGetContributor() throws Exception {
        // Given
        Long contributorId = 1L;
        ContributorDetailResponse mockResponse = new ContributorDetailResponse(contributorId, "Contributor Name", "Contributor Description");

        when(contributorService.getContributor(anyLong())).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(get("/api/admin/contributors/{contributor-id}", contributorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.id").value(1L)) // 기여자 ID 확인
                .andExpect(jsonPath("$.name").value("Contributor Name")) // 기여자 이름 확인
                .andExpect(jsonPath("$.description").value("Contributor Description")); // 기여자 설명 확인

        verify(contributorService, times(1)).getContributor(anyLong()); // 서비스 호출 검증
    }

    @Test
    void testUpdateContributor() throws Exception {
        // Given
        Long contributorId = 1L;
        ContributorUpdateRequest request = new ContributorUpdateRequest("Updated Contributor Name", "Updated Description");
        ContributorUpdateResponse mockResponse = new ContributorUpdateResponse("Updated Contributor Name", "Updated Description");

        when(contributorService.updateContributor(anyLong(), any(ContributorUpdateRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(put("/api/admin/contributors/{contributor-id}", contributorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.name").value("Updated Contributor Name")) // 업데이트된 기여자 이름 확인
                .andExpect(jsonPath("$.description").value("Updated Description")); // 업데이트된 기여자 설명 확인

        verify(contributorService, times(1)).updateContributor(anyLong(), any(ContributorUpdateRequest.class)); // 서비스 호출 검증
    }

    @Test
    void testDeleteContributor() throws Exception {
        // Given
        Long contributorId = 1L;
        ContributorDeleteResponse mockResponse = new ContributorDeleteResponse("Deleted Contributor Name");

        when(contributorService.deleteContributor(anyLong())).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(delete("/api/admin/contributors/{contributor-id}", contributorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.name").value("Deleted Contributor Name")); // 삭제된 기여자 이름 확인

        verify(contributorService, times(1)).deleteContributor(anyLong()); // 서비스 호출 검증
    }

}
