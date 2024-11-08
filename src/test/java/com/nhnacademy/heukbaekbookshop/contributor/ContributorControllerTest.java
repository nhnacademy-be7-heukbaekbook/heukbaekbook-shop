package com.nhnacademy.heukbaekbookshop.contributor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekbookshop.contributor.controller.ContributorController;
import com.nhnacademy.heukbaekbookshop.contributor.dto.request.ContributorCreateRequest;
import com.nhnacademy.heukbaekbookshop.contributor.dto.request.ContributorUpdateRequest;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorCreateResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorDeleteResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorDetailResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorUpdateResponse;
import com.nhnacademy.heukbaekbookshop.contributor.exception.ContributorAlreadyExistException;
import com.nhnacademy.heukbaekbookshop.contributor.exception.ContributorNotFoundException;
import com.nhnacademy.heukbaekbookshop.contributor.service.ContributorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContributorController.class)
public class ContributorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContributorService contributorService;

    @Autowired
    private ObjectMapper objectMapper;

    private ContributorCreateRequest createRequest;
    private ContributorCreateResponse createResponse;
    private ContributorDetailResponse detailResponse;
    private ContributorUpdateRequest updateRequest;
    private ContributorUpdateResponse updateResponse;
    private ContributorDeleteResponse deleteResponse;

    @BeforeEach
    public void setUp() {
        createRequest = new ContributorCreateRequest("Jane Smith", "Mystery Writer");
        createResponse = new ContributorCreateResponse("Jane Smith", "Mystery Writer");

        detailResponse = new ContributorDetailResponse("John Doe", "Author of fantasy novels");

        updateRequest = new ContributorUpdateRequest("John Updated", "Updated Description");
        updateResponse = new ContributorUpdateResponse("John Updated", "Updated Description");

        deleteResponse = new ContributorDeleteResponse("John Doe");
    }

    @Test
    public void registerContributor_Success() throws Exception {
        // Given
        when(contributorService.registerContributor(any(ContributorCreateRequest.class))).thenReturn(createResponse);

        // When & Then
        mockMvc.perform(post("/api/contributors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(createResponse.name()))
                .andExpect(jsonPath("$.description").value(createResponse.description()));

        verify(contributorService, times(1)).registerContributor(any(ContributorCreateRequest.class));
    }

    @Test
    public void registerContributor_ContributorAlreadyExists() throws Exception {
        // Given
        when(contributorService.registerContributor(any(ContributorCreateRequest.class)))
                .thenThrow(new ContributorAlreadyExistException("Contributor already exists"));

        // When & Then
        mockMvc.perform(post("/api/contributors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().is4xxClientError());

        verify(contributorService, times(1)).registerContributor(any(ContributorCreateRequest.class));
    }

    @Test
    public void getContributor_Success() throws Exception {
        // Given
        Long contributorId = 1L;
        when(contributorService.getContributor(contributorId)).thenReturn(detailResponse);

        // When & Then
        mockMvc.perform(get("/api/contributors/{id}", contributorId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(detailResponse.name()))
                .andExpect(jsonPath("$.description").value(detailResponse.description()));

        verify(contributorService, times(1)).getContributor(contributorId);
    }

    @Test
    public void getContributor_NotFound() throws Exception {
        // Given
        Long contributorId = 2L;
        when(contributorService.getContributor(contributorId))
                .thenThrow(new ContributorNotFoundException("Contributor not found"));

        // When & Then
        mockMvc.perform(get("/api/contributors/{id}", contributorId))
                .andExpect(status().is4xxClientError());

        verify(contributorService, times(1)).getContributor(contributorId);
    }

    @Test
    public void updateContributor_Success() throws Exception {
        // Given
        Long contributorId = 1L;
        when(contributorService.updateContributor(eq(contributorId), any(ContributorUpdateRequest.class)))
                .thenReturn(updateResponse);

        // When & Then
        mockMvc.perform(put("/api/contributors/{id}", contributorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(updateResponse.name()))
                .andExpect(jsonPath("$.description").value(updateResponse.description()));

        verify(contributorService, times(1)).updateContributor(eq(contributorId), any(ContributorUpdateRequest.class));
    }

    @Test
    public void updateContributor_NotFound() throws Exception {
        // Given
        Long contributorId = 2L;
        when(contributorService.updateContributor(eq(contributorId), any(ContributorUpdateRequest.class)))
                .thenThrow(new ContributorNotFoundException("Contributor not found"));

        // When & Then
        mockMvc.perform(put("/api/contributors/{id}", contributorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().is4xxClientError());

        verify(contributorService, times(1)).updateContributor(eq(contributorId), any(ContributorUpdateRequest.class));
    }

    @Test
    public void deleteContributor_Success() throws Exception {
        // Given
        Long contributorId = 1L;
        when(contributorService.deleteContributor(contributorId)).thenReturn(deleteResponse);

        // When & Then
        mockMvc.perform(delete("/api/contributors/{id}", contributorId))
                .andExpect(status().isNoContent());

        verify(contributorService, times(1)).deleteContributor(contributorId);
    }

    @Test
    public void deleteContributor_NotFound() throws Exception {
        // Given
        Long contributorId = 2L;
        when(contributorService.deleteContributor(contributorId))
                .thenThrow(new ContributorNotFoundException("Contributor not found"));

        // When & Then
        mockMvc.perform(delete("/api/contributors/{id}", contributorId))
                .andExpect(status().is4xxClientError());

        verify(contributorService, times(1)).deleteContributor(contributorId);
    }
}
