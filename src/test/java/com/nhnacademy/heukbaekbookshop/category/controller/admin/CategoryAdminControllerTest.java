package com.nhnacademy.heukbaekbookshop.category.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekbookshop.category.dto.request.CategoryCreateRequest;
import com.nhnacademy.heukbaekbookshop.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryCreateResponse;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryDeleteResponse;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryDetailResponse;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryUpdateResponse;
import com.nhnacademy.heukbaekbookshop.category.service.CategoryService;
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

@WebMvcTest(CategoryAdminController.class)
class CategoryAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCategory() throws Exception {
        // Given
        CategoryCreateRequest request = new CategoryCreateRequest(1L, "Fiction");
        CategoryCreateResponse mockResponse = new CategoryCreateResponse(1L, "Fiction");

        when(categoryService.registerCategory(any(CategoryCreateRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.parentId").value(1L)) // 부모 ID 확인
                .andExpect(jsonPath("$.name").value("Fiction")); // 카테고리 이름 확인

        verify(categoryService, times(1)).registerCategory(any(CategoryCreateRequest.class)); // 서비스 호출 검증
    }

    @Test
    void testUpdateCategory() throws Exception {
        // Given
        Long categoryId = 1L;
        CategoryUpdateRequest request = new CategoryUpdateRequest(1l,"Fiction");
        CategoryUpdateResponse mockResponse = new CategoryUpdateResponse(categoryId, "Updated Fiction");

        when(categoryService.updateCategory(anyLong(), any(CategoryUpdateRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(put("/api/admin/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.parentId").value(1L)) // 카테고리 ID 확인
                .andExpect(jsonPath("$.name").value("Updated Fiction")); // 카테고리 이름 확인

        verify(categoryService, times(1)).updateCategory(anyLong(), any(CategoryUpdateRequest.class)); // 서비스 호출 검증
    }

    @Test
    void testDeleteCategory() throws Exception {
        Long categoryId = 1L;
        CategoryDeleteResponse mockResponse = new CategoryDeleteResponse("Deleted Fiction");

        when(categoryService.deleteCategory(anyLong())).thenReturn(mockResponse);

        mockMvc.perform(delete("/api/admin/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.message").value("Deleted Fiction")); // 삭제 메시지 확인

        verify(categoryService, times(1)).deleteCategory(anyLong()); // 서비스 호출 검증
    }

    @Test
    void testGetCategory() throws Exception {
        // Given
        Long categoryId = 1L;
        CategoryDetailResponse mockResponse = new CategoryDetailResponse(categoryId, 0L, "Fiction");

        when(categoryService.getCategory(anyLong())).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(get("/api/admin/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.parentId").value(0L))
                .andExpect(jsonPath("$.name").value("Fiction"));

        verify(categoryService, times(1)).getCategory(anyLong());
    }

    @Test
    void testGetCategories() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<CategoryDetailResponse> categories = List.of(
                new CategoryDetailResponse(1L, 0L, "Fiction"),
                new CategoryDetailResponse(2L, 1L, "Science Fiction")
        );

        Page<CategoryDetailResponse> mockPage = new PageImpl<>(categories, pageable, categories.size());

        when(categoryService.getCategories(any(Pageable.class))).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/api/admin/categories")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.content[0].id").value(1L)) // 첫 번째 카테고리 ID 확인
                .andExpect(jsonPath("$.content[0].parentId").value(0L)) // 첫 번째 카테고리 부모 ID 확인
                .andExpect(jsonPath("$.content[0].name").value("Fiction")) // 첫 번째 카테고리 이름 확인
                .andExpect(jsonPath("$.content[1].id").value(2L)) // 두 번째 카테고리 ID 확인
                .andExpect(jsonPath("$.content[1].parentId").value(1L)) // 두 번째 카테고리 부모 ID 확인
                .andExpect(jsonPath("$.content[1].name").value("Science Fiction")); // 두 번째 카테고리 이름 확인

        verify(categoryService, times(1)).getCategories(any(Pageable.class)); // 서비스 호출 검증
    }
    @Test
    void testGetCategoryPaths() throws Exception {
        // Given
        List<String> mockResponses = List.of(
                "Fiction > Science Fiction",
                "Fiction > Mystery",
                "Non-Fiction > Biography"
        );

        when(categoryService.getCategoryPaths()).thenReturn(mockResponses);

        // When & Then
        mockMvc.perform(get("/api/admin/categories/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$[0]").value("Fiction > Science Fiction")) // 첫 번째 카테고리 경로 확인
                .andExpect(jsonPath("$[1]").value("Fiction > Mystery")) // 두 번째 카테고리 경로 확인
                .andExpect(jsonPath("$[2]").value("Non-Fiction > Biography")); // 세 번째 카테고리 경로 확인

        verify(categoryService, times(1)).getCategoryPaths(); // 서비스 호출 검증
    }

}
