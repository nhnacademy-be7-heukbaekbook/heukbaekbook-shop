package com.nhnacademy.heukbaekbookshop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekbookshop.category.controller.CategoryController;
import com.nhnacademy.heukbaekbookshop.category.dto.request.CategoryCreateRequest;
import com.nhnacademy.heukbaekbookshop.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryCreateResponse;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryDeleteResponse;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryUpdateResponse;
import com.nhnacademy.heukbaekbookshop.category.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    // 카테고리 등록 성공 테스트
    @Test
    public void testCreateCategory_Success() throws Exception {
        // Given
        CategoryCreateRequest request = new CategoryCreateRequest(null, "New Category");
        String requestJson = objectMapper.writeValueAsString(request);

        when(categoryService.registerCategory(any(CategoryCreateRequest.class)))
                .thenReturn(new CategoryCreateResponse(null, "New Category"));

        // When & Then
        mockMvc.perform(post("/categories/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.parentId").doesNotExist())
                .andExpect(jsonPath("$.name").value("New Category"));
    }

    // 카테고리 업데이트 성공 테스트
    @Test
    public void testUpdateCategory_Success() throws Exception {
        // Given
        CategoryUpdateRequest request = new CategoryUpdateRequest(null, "Updated Category");
        String requestJson = objectMapper.writeValueAsString(request);

        when(categoryService.updateCategory(eq(1L), any(CategoryUpdateRequest.class)))
                .thenReturn(new CategoryUpdateResponse(null, "Updated Category"));

        // When & Then
        mockMvc.perform(put("/categories/admins/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parentId").doesNotExist())
                .andExpect(jsonPath("$.name").value("Updated Category"));
    }

    // 카테고리 삭제 성공 테스트
    @Test
    public void testDeleteCategory_Success() throws Exception {
        // Given
        when(categoryService.deleteCategory(1L))
                .thenReturn(new CategoryDeleteResponse("카테고리가 정상적으로 삭제되었습니다."));

        // When & Then
        mockMvc.perform(delete("/categories/admins/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("카테고리가 정상적으로 삭제되었습니다."));
    }
}
