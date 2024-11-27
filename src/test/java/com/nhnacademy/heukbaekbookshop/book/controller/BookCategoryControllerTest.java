package com.nhnacademy.heukbaekbookshop.book.controller;

import com.nhnacademy.heukbaekbookshop.book.service.book.BookCategoryService;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategorySummaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookCategoryController.class)
class BookCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookCategoryService bookCategoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBookCategoriesByBookId() throws Exception {
        // Given
        Long bookId = 1L;

        CategorySummaryResponse subCategory = new CategorySummaryResponse(
                2L,
                "Subcategory Name",
                List.of()
        );

        CategorySummaryResponse category = new CategorySummaryResponse(
                1L,
                "Main Category",
                List.of(subCategory)
        );

        when(bookCategoryService.findBookCategoriesByBookId(anyLong()))
                .thenReturn(List.of());

        // When & Then
        mockMvc.perform(get("/api/books/{bookId}/book-categories", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookCategoryService, times(1)).findBookCategoriesByBookId(anyLong());
    }
}
