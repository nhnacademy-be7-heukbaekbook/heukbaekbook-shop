package com.nhnacademy.heukbaekbookshop.category.controller;

import com.nhnacademy.heukbaekbookshop.category.dto.response.CategorySummaryResponse;
import com.nhnacademy.heukbaekbookshop.category.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetParentCategories() throws Exception {
        // Given
        CategorySummaryResponse subCategory = new CategorySummaryResponse(2L, "Subcategory", List.of());
        CategorySummaryResponse parentCategory = new CategorySummaryResponse(1L, "ParentCategory", List.of(subCategory));

        when(categoryService.getTopCategories()).thenReturn(List.of(parentCategory));

        // When & Then
        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$[0].id").value(1L)) // 첫 번째 카테고리 ID 확인
                .andExpect(jsonPath("$[0].name").value("ParentCategory")) // 첫 번째 카테고리 이름 확인
                .andExpect(jsonPath("$[0].subCategorySummaryResponses[0].id").value(2L)) // 서브카테고리 ID 확인
                .andExpect(jsonPath("$[0].subCategorySummaryResponses[0].name").value("Subcategory")); // 서브카테고리 이름 확인

        verify(categoryService, times(1)).getTopCategories(); // 서비스 호출 검증
    }
}
