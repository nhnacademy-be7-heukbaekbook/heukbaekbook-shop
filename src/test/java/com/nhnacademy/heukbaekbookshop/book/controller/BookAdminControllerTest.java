package com.nhnacademy.heukbaekbookshop.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookCreateRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookUpdateRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.*;
import com.nhnacademy.heukbaekbookshop.book.service.book.BookService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookAdminController.class)
class BookAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchBooks() throws Exception {
        // Given
        String title = "Test Title";
        List<BookSearchResponse> mockResponses = List.of(
                new BookSearchResponse(
                        "Test Book 1",
                        "cover1.png",
                        "Description of Test Book 1",
                        "Fiction",
                        "Author1",
                        "Publisher1",
                        LocalDate.of(2022, 1, 1),
                        "1234567890",
                        20000,
                        18000
                ),
                new BookSearchResponse(
                        "Test Book 2",
                        "cover2.png",
                        "Description of Test Book 2",
                        "Non-Fiction",
                        "Author2",
                        "Publisher2",
                        LocalDate.of(2021, 5, 15),
                        "0987654321",
                        30000,
                        27000
                )
        );

        when(bookService.searchBook(title)).thenReturn(mockResponses);

        // When & Then
        mockMvc.perform(post("/api/admin/aladin")
                        .param("title", title))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Book 1"))
                .andExpect(jsonPath("$[0].author").value("Author1"))
                .andExpect(jsonPath("$[1].title").value("Test Book 2"))
                .andExpect(jsonPath("$[1].publisher").value("Publisher2"));

        verify(bookService, times(1)).searchBook(title);
    }

    @Test
    void testRegisterBook() throws Exception {
        // Given: BookCreateRequest 객체 생성 및 필드 값 설정
        BookCreateRequest request = new BookCreateRequest(
                "Test Title",
                "Test Index",
                "Test Description",
                "2024-01-01",
                "123456789",
                "url",
                true,
                50,
                20000,
                new BigDecimal("0.15"),
                "Test Publisher",
                "Fiction, Mystery",
                "Author1, Author2"
        );

        // Mock 응답 설정
        BookCreateResponse mockResponse = new BookCreateResponse(
                "Test Title",
                "Test Index",
                "Test Description",
                "2024-01-01",
                "123456789",
                true,
                50,
                20000,
                new BigDecimal("0.15"),
                "Test Publisher",
                List.of("Fiction", "Mystery"),
                List.of("Author1", "Author2")
        );

        when(bookService.registerBook(any(BookCreateRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/admin/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))) // 요청 JSON 직렬화
                .andExpect(status().isCreated()) // 응답 상태 코드가 201 CREATED인지 확인
                .andExpect(jsonPath("$.title").value("Test Title")) // 응답 JSON 필드 검증
                .andExpect(jsonPath("$.publisher").value("Test Publisher"))
                .andExpect(jsonPath("$.categories[0]").value("Fiction"))
                .andExpect(jsonPath("$.categories[1]").value("Mystery"))
                .andExpect(jsonPath("$.authors[0]").value("Author1"))
                .andExpect(jsonPath("$.authors[1]").value("Author2"));

        verify(bookService, times(1)).registerBook(any(BookCreateRequest.class)); // 서비스 호출 검증
    }

    @Test
    void testUpdateBook() throws Exception {
        // Given: BookUpdateRequest 객체 생성
        BookUpdateRequest request = new BookUpdateRequest(
                "Updated Title",
                "Updated Index",
                "Updated Description",
                "2024-01-01",
                "123456789",
                "updated-image.png",
                List.of(),
                true,
                100,
                25000,
                new BigDecimal("0.10"),
                "Available",
                "Updated Publisher",
                List.of(),
                "Author1, Author3",
                List.of()
        );

        // Mock 응답 설정
        BookUpdateResponse mockResponse = new BookUpdateResponse(
                "Updated Title",
                "Updated Index",
                "Updated Description",
                "2024-01-01",
                "123456789",
                "updated-image.png",
                true,
                100,
                25000,
                new BigDecimal("0.10"),
                "Available",
                "Updated Publisher",
                List.of("Fiction", "Adventure"),
                List.of("Author1", "Author3")
        );

        when(bookService.updateBook(anyLong(), any(BookUpdateRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(put("/api/admin/books/{bookId}", 1L) // PUT 요청
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))) // 요청 JSON 직렬화
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.publisher").value("Updated Publisher"))
                .andExpect(jsonPath("$.categories[0]").value("Fiction"))
                .andExpect(jsonPath("$.categories[1]").value("Adventure"))
                .andExpect(jsonPath("$.authors[0]").value("Author1"))
                .andExpect(jsonPath("$.authors[1]").value("Author3"));

        verify(bookService, times(1)).updateBook(anyLong(), any(BookUpdateRequest.class)); // 서비스 호출 검증
    }

    @Test
    void testDeleteBook() throws Exception {
        // Given
        Long bookId = 1L;
        BookDeleteResponse mockResponse = new BookDeleteResponse("Book successfully deleted.");

        when(bookService.deleteBook(anyLong())).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(delete("/api/admin/books/{bookId}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드가 200 OK인지 확인
                .andExpect(jsonPath("$.message").value("Book successfully deleted.")); // 응답 메시지 확인

        verify(bookService, times(1)).deleteBook(anyLong()); // 서비스 호출 검증
    }

    @Test
    void testGetBooks() throws Exception {
        // Given
        BookDetailResponse mockBook = new BookDetailResponse(
                1L,
                "Sample Book",
                "Sample Index",
                "Sample Description",
                "2024-01-01",
                "123456789",
                "sample-thumbnail.png",
                List.of("detail-image-1.png", "detail-image-2.png"),
                true,
                100,
                20000,
                new BigDecimal("0.15"),
                "Available",
                "Sample Publisher",
                List.of("Fiction", "Adventure"),
                List.of("Author1", "Author2"),
                List.of("Tag1", "Tag2")
        );

        Page<BookDetailResponse> mockResponse = new PageImpl<>(List.of(mockBook), PageRequest.of(0, 10), 1);

        when(bookService.getBooksDetail(any(PageRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(get("/api/admin/books")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드가 200 OK인지 확인
                .andExpect(jsonPath("$.content[0].title").value("Sample Book"));


        verify(bookService, times(1)).getBooksDetail(any(PageRequest.class)); // 서비스 호출 검증
    }
}
