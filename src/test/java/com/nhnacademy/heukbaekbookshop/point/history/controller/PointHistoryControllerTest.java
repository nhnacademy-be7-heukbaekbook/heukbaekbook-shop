package com.nhnacademy.heukbaekbookshop.point.history.controller;

import com.nhnacademy.heukbaekbookshop.point.history.controller.PointHistoryController;
import com.nhnacademy.heukbaekbookshop.point.history.domain.PointType;
import com.nhnacademy.heukbaekbookshop.point.history.dto.response.PointHistoryResponse;
import com.nhnacademy.heukbaekbookshop.point.history.service.PointHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PointHistoryController.class)
class PointHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointHistoryService pointHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new PointHistoryController(pointHistoryService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testGetPointHistoriesByMemberId_Success() throws Exception {
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        PointHistoryResponse history = new PointHistoryResponse(
                1L,
                memberId,
                100L,
                "Earned Points",
                new BigDecimal("1000"),
                LocalDateTime.now(),
                new BigDecimal("2000"),
                PointType.EARNED
        );
        Page<PointHistoryResponse> page = new PageImpl<>(List.of(history), pageable, 1);

        when(pointHistoryService.getPointHistoriesByCustomerId(eq(memberId), eq(pageable))).thenReturn(page);

        mockMvc.perform(get("/api/points/histories")
                        .header("X-USER-ID", memberId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(history.id()))
                .andExpect(jsonPath("$.content[0].customerId").value(memberId))
                .andExpect(jsonPath("$.content[0].amount").value("1000"))
                .andExpect(jsonPath("$.content[0].balance").value("2000"));
    }

    @Test
    void testGetCurrentBalanceByMemberId_Success() throws Exception {
        Long memberId = 1L;
        BigDecimal balance = new BigDecimal("2000");

        when(pointHistoryService.getCurrentBalanceByCustomerId(eq(memberId))).thenReturn(balance);

        mockMvc.perform(get("/api/points/balance")
                        .header("X-USER-ID", memberId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(balance.toPlainString()));
    }
}
