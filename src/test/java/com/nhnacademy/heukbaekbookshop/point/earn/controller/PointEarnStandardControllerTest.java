package com.nhnacademy.heukbaekbookshop.point.earn.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekbookshop.point.earn.controller.PointEarnStandardController;
import com.nhnacademy.heukbaekbookshop.point.earn.domain.EventCode;
import com.nhnacademy.heukbaekbookshop.point.earn.domain.PointEarnStandardStatus;
import com.nhnacademy.heukbaekbookshop.point.earn.domain.PointEarnType;
import com.nhnacademy.heukbaekbookshop.point.earn.dto.request.PointEarnStandardRequest;
import com.nhnacademy.heukbaekbookshop.point.earn.dto.response.PointEarnStandardResponse;
import com.nhnacademy.heukbaekbookshop.point.earn.service.PointEarnStandardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointEarnStandardController.class)
class PointEarnStandardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PointEarnStandardService pointEarnStandardService;

    private PointEarnStandardRequest validRequest;
    private PointEarnStandardResponse validResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        validRequest = new PointEarnStandardRequest(
                "Signup Points",
                new BigDecimal("100"),
                PointEarnType.FIXED,
                PointEarnStandardStatus.ACTIVATED,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                1L
        );

        validResponse = new PointEarnStandardResponse(
                1L,
                "Signup Points",
                new BigDecimal("100"),
                PointEarnStandardStatus.ACTIVATED,
                PointEarnType.FIXED,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                null
        );
    }

    @Test
    void testCreatePointEarnStandard_Success() throws Exception {
        when(pointEarnStandardService.createPointEarnStandard(validRequest))
                .thenReturn(validResponse);

        mockMvc.perform(post("/api/admin/points/earn-standards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(validResponse)));
    }

    @Test
    void testGetValidStandardsByEvent_Success() throws Exception {
        String eventCode = "SIGNUP";

        List<PointEarnStandardResponse> responses = List.of(
                new PointEarnStandardResponse(
                        1L,
                        "Signup Points",
                        new BigDecimal("100"),
                        PointEarnStandardStatus.ACTIVATED,
                        PointEarnType.FIXED,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(30),
                        EventCode.SIGNUP
                )
        );

        when(pointEarnStandardService.getValidStandardsByEvent(EventCode.valueOf(eventCode.toUpperCase())))
                .thenReturn(responses);

        mockMvc.perform(get("/api/admin/points/earn-standards/event/{eventCode}", eventCode))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }

    @Test
    void testUpdatePointEarnStandard_Success() throws Exception {
        Long id = 1L;

        PointEarnStandardRequest request = new PointEarnStandardRequest(
                "Updated Name",
                new BigDecimal("200"),
                PointEarnType.PERCENTAGE,
                PointEarnStandardStatus.ACTIVATED,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(10),
                2L
        );

        PointEarnStandardResponse response = new PointEarnStandardResponse(
                id,
                "Updated Name",
                new BigDecimal("200"),
                PointEarnStandardStatus.ACTIVATED,
                PointEarnType.PERCENTAGE,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(10),
                EventCode.ORDER
        );

        when(pointEarnStandardService.updatePointEarnStandard(eq(id), any(PointEarnStandardRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/admin/points/earn-standards/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(pointEarnStandardService).updatePointEarnStandard(eq(id), any(PointEarnStandardRequest.class));
    }

    @Test
    void testDeletePointEarnStandard_Success() throws Exception {
        Long id = 1L;

        // Perform DELETE request
        mockMvc.perform(delete("/api/admin/points/earn-standards/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verify service method is called
        verify(pointEarnStandardService).deletePointEarnStandard(eq(id));
    }

}