package com.nhnacademy.heukbaekbookshop.memberset.grade.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekbookshop.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekbookshop.memberset.grade.exception.GradeNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.grade.service.GradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GradeController.class)
class GradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GradeService gradeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetGrade() throws Exception {
        // Mock GradeDto 데이터 생성
        GradeDto mockGrade = new GradeDto(
                "Gold",
                new BigDecimal("0.1"),
                new BigDecimal("1000")
        );

        // Mock 서비스 동작 설정
        when(gradeService.getGrade(anyLong())).thenReturn(mockGrade);

        // 테스트 수행
        mockMvc.perform(get("/api/grades/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gradeName").value("Gold"))
                .andExpect(jsonPath("$.pointPercentage").value("0.1"))
                .andExpect(jsonPath("$.promotionStandard").value("1000"));
    }

    @Test
    void testGetAllGrades() throws Exception {
        // Mock GradeDto 리스트 생성
        List<GradeDto> mockGrades = List.of(
                new GradeDto("Gold", new BigDecimal("0.1"), new BigDecimal("1000.0")),
                new GradeDto("Silver", new BigDecimal("0.05"), new BigDecimal("500.0"))
        );

        // Mock 서비스 동작 설정
        when(gradeService.getAllGradeList()).thenReturn(mockGrades);

        // 테스트 수행
        mockMvc.perform(get("/api/grades")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].gradeName").value("Gold"))
                .andExpect(jsonPath("$[0].pointPercentage").value("0.1"))
                .andExpect(jsonPath("$[0].promotionStandard").value("1000.0"))
                .andExpect(jsonPath("$[1].gradeName").value("Silver"))
                .andExpect(jsonPath("$[1].pointPercentage").value("0.05"))
                .andExpect(jsonPath("$[1].promotionStandard").value("500.0"));
    }

    @Test
    void testCreateGradeWithValidData() throws Exception {
        // 유효한 GradeDto 데이터 생성
        GradeDto validGradeDto = new GradeDto(
                "Gold",
                new BigDecimal("0.1"),
                new BigDecimal("1000.0")
        );

        // Mock GradeDto 생성
        GradeDto mockResponse = new GradeDto(
                "Gold",
                new BigDecimal("0.1"),
                new BigDecimal("1000.0")
        );

        // Mock 서비스 동작 설정
        when(gradeService.createGrade(any(GradeDto.class))).thenReturn(mockResponse);

        // 테스트 수행
        mockMvc.perform(post("/api/admin/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validGradeDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.gradeName").value("Gold"))
                .andExpect(jsonPath("$.pointPercentage").value("0.1"))
                .andExpect(jsonPath("$.promotionStandard").value("1000.0"));
    }

    @Test
    void testUpdateGradeWithValidData() throws Exception {
        // 유효한 GradeDto 데이터 생성
        GradeDto validGradeDto = new GradeDto(
                "Platinum",
                new BigDecimal("0.15"),
                new BigDecimal("2000.0")
        );

        // Mock GradeDto 생성
        GradeDto mockResponse = new GradeDto(
                "Platinum",
                new BigDecimal("0.15"),
                new BigDecimal("2000.0")
        );

        // Mock 서비스 동작 설정
        when(gradeService.updateGrade(anyLong(), any(GradeDto.class))).thenReturn(mockResponse);

        // 테스트 수행
        mockMvc.perform(put("/api/admin/grades/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validGradeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gradeName").value("Platinum"))
                .andExpect(jsonPath("$.pointPercentage").value("0.15"))
                .andExpect(jsonPath("$.promotionStandard").value("2000.0"));
    }

    @Test
    void testDeleteGrade() throws Exception {
        // Mock 서비스 동작 설정
        doNothing().when(gradeService).deleteGrade(anyLong());

        // 테스트 수행
        mockMvc.perform(delete("/api/admin/grades/1"))
                .andExpect(status().isNoContent());
    }
}
