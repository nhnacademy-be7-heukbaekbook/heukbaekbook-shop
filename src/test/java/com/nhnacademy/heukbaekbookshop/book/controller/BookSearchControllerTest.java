package com.nhnacademy.heukbaekbookshop.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookResponse;
import com.nhnacademy.heukbaekbookshop.book.service.book.BookSearchService;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorSummaryResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherSummaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookSearchController.class)
class BookSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookSearchService bookSearchService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchBooks() throws Exception {
        // Given
        BookSearchRequest searchRequest = new BookSearchRequest("Sample Title", "Author Name", "Publisher Name", 1L);
        List<BookResponse> bookResponses = List.of(
                new BookResponse(
                        1L,
                        "Book Title 1",
                        "2024-01-01",
                        "18000",
                        new BigDecimal("0.10"),
                        "thumbnail1.png",
                        List.of(new ContributorSummaryResponse(1L, "Author1"), new ContributorSummaryResponse(2L, "Author2")),
                        new PublisherSummaryResponse(1L, "ABC")
                )
        );

        Page<BookResponse> mockPage = new PageImpl<>(bookResponses, PageRequest.of(0, 10), bookResponses.size());

        when(bookSearchService.searchBooks(any(PageRequest.class), any(BookSearchRequest.class))).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(post("/api/books/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L));

        verify(bookSearchService, times(1)).searchBooks(any(PageRequest.class), any(BookSearchRequest.class));
    }
}
