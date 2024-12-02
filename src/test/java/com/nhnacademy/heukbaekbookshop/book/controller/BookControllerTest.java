package com.nhnacademy.heukbaekbookshop.book.controller;

import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookDetailResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookSummaryResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookViewResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.like.LikeCreateResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.like.LikeDeleteResponse;
import com.nhnacademy.heukbaekbookshop.book.service.book.BookService;
import com.nhnacademy.heukbaekbookshop.book.service.like.LikeService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private LikeService likeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBook() throws Exception {
        // Given
        Long bookId = 1L;
        BookDetailResponse mockResponse = new BookDetailResponse(
                bookId,
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

        when(bookService.getBook(anyLong())).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(get("/api/books/{bookId}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.id").value(1L));

        verify(bookService, times(1)).getBook(anyLong());
    }

    @Test
    void testCreateLike() throws Exception {
        // Given
        Long bookId = 1L;
        Long customerId = 2L;
        LikeCreateResponse mockResponse = new LikeCreateResponse("Like successfully created.");

        when(likeService.createLike(anyLong(), anyLong())).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/books/{bookId}/likes", bookId)
                        .param("customerId", customerId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.message").value("Like successfully created.")); // 응답 메시지 확인

        verify(likeService, times(1)).createLike(anyLong(), anyLong()); // 서비스 호출 검증
    }

    @Test
    void testDeleteLike() throws Exception {
        // Given
        Long bookId = 1L;
        Long customerId = 2L;
        LikeDeleteResponse mockResponse = new LikeDeleteResponse("Like successfully deleted.");

        when(likeService.deleteLike(anyLong(), anyLong())).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(delete("/api/books/{bookId}/likes", bookId)
                        .param("customerId", customerId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.message").value("Like successfully deleted.")); // 응답 메시지 확인

        verify(likeService, times(1)).deleteLike(anyLong(), anyLong()); // 서비스 호출 검증
    }

    @Test
    void testGetBooksSummary() throws Exception {
        // Given
        List<Long> bookIds = List.of(1L, 2L);
        List<BookSummaryResponse> mockResponse = List.of(
                new BookSummaryResponse(
                        1L,
                        "Book Title 1",
                        true,
                        new BigDecimal("20000"),
                        new BigDecimal("18000"),
                        new BigDecimal("0.10"),
                        "thumbnail1.png"
                ),
                new BookSummaryResponse(
                        2L,
                        "Book Title 2",
                        false,
                        new BigDecimal("30000"),
                        new BigDecimal("27000"),
                        new BigDecimal("0.10"),
                        "thumbnail2.png"
                )
        );

        when(bookService.getBooksSummary(anyList())).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(get("/api/books/summary")
                        .param("bookIds", "1", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$[0].id").value(1L)) // 첫 번째 책 ID 확인
                .andExpect(jsonPath("$[0].title").value("Book Title 1")) // 첫 번째 책 제목 확인
                .andExpect(jsonPath("$[0].price").value(20000)) // 첫 번째 책 가격 확인
                .andExpect(jsonPath("$[1].id").value(2L)) // 두 번째 책 ID 확인
                .andExpect(jsonPath("$[1].title").value("Book Title 2")) // 두 번째 책 제목 확인
                .andExpect(jsonPath("$[1].thumbnailUrl").value("thumbnail2.png")); // 두 번째 책 썸네일 URL 확인

        verify(bookService, times(1)).getBooksSummary(anyList()); // 서비스 호출 검증
    }

    @Test
    void testGetBooks() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<BookResponse> bookResponses = List.of(
                new BookResponse(
                        1L,
                        "Book Title 1",
                        "2024-01-01",
                        "18000",
                        new BigDecimal("0.10"),
                        "thumbnail1.png",
                        List.of(new ContributorSummaryResponse(1L,"DD"), new ContributorSummaryResponse(2L, "DDD")),
                        new PublisherSummaryResponse(3L,"ABC")
                )
        );

        Page<BookResponse> mockPage = new PageImpl<>(bookResponses, pageable, bookResponses.size());

        when(bookService.getBooks(any(Pageable.class))).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/api/books")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.content[0].id").value(1L)); // 첫 번째 책 ID 확인


        verify(bookService, times(1)).getBooks(any(Pageable.class)); // 서비스 호출 검증
    }

    @Test
    void testGetBooksByCategoryId() throws Exception {
        // Given
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<BookResponse> bookResponses = List.of(
                new BookResponse(
                        1L,
                        "Book Title 1",
                        "2024-01-01",
                        "18000",
                        new BigDecimal("0.10"),
                        "thumbnail1.png",
                        List.of(new ContributorSummaryResponse(1L, "DD"), new ContributorSummaryResponse(2L, "DDD")),
                        new PublisherSummaryResponse(1L,"ABC")
                )
        );

        Page<BookResponse> mockPage = new PageImpl<>(bookResponses, pageable, bookResponses.size());

        when(bookService.getBooksByCategoryId(anyLong(), any(Pageable.class))).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/api/books/categories/{categoryId}", categoryId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.content[0].id").value(1L));

        verify(bookService, times(1)).getBooksByCategoryId(anyLong(), any(Pageable.class)); // 서비스 호출 검증
    }

    @Test
    void testGetBookDetail() throws Exception {
        // Given
        Long bookId = 1L;
        BookViewResponse mockResponse = new BookViewResponse(
                bookId,
                "Sample Book",
                "Sample Index",
                "Sample Description",
                "2024-01-01",
                "123456789",
                true,
                "20000",
                "18000",
                new BigDecimal("0.10"),
                100,
                "Available",
                "thumbnail1.png",
                List.of("image1.png", "image2.png"),
                List.of(new ContributorSummaryResponse(1L, "Author1"), new ContributorSummaryResponse(2L, "Author2")),
                new PublisherSummaryResponse(1L,"AB")
        );

        when(bookService.getBookDetail(anyLong())).thenReturn(mockResponse);
        doNothing().when(bookService).increasePopularity(anyLong());

        // When & Then
        mockMvc.perform(get("/api/books/detail")
                        .param("bookId", bookId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(bookService, times(1)).increasePopularity(anyLong());
        verify(bookService, times(1)).getBookDetail(anyLong());
    }

}
