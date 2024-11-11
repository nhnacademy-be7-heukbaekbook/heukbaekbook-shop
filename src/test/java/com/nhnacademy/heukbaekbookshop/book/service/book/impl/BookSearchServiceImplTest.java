package com.nhnacademy.heukbaekbookshop.book.service.book.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.nhnacademy.heukbaekbookshop.book.domain.document.BookDocument;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookElasticSearchResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookResponse;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookDocumentRepository;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookSearchRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class BookSearchServiceImplTest {

    @Mock
    BookSearchRepository bookSearchRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    BookDocumentRepository bookDocumentRepository;

    @InjectMocks
    BookSearchServiceImpl bookSearchService;

    @Test
    @DisplayName("도서명 기준 검색 테스트")
    void searchBooksByTitle() {
        PageRequest pageable = PageRequest.of(0, 5);
        BookSearchRequest searchRequest = new BookSearchRequest("자바", "title", "newest");
        BookDocument bookDocument = new BookDocument(1L, "자바의 정석", "자바 프로그래밍 입문서", "남궁성",
                "1234567890123", new java.util.Date(), "출판사", "35000",
                10.0f, 100L, List.of("자바", "프로그래밍"));
        PageImpl<BookDocument> searchResponse = new PageImpl<>(List.of(bookDocument), pageable, 1);

        when(bookSearchRepository.search(any(), any(), any(), any())).thenReturn(searchResponse);

        Page<BookResponse> result = bookSearchService.searchBooks(pageable, searchRequest);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("도서 설명 기준 검색 테스트")
    void searchBooksByDescription() {
        PageRequest pageable = PageRequest.of(0, 5);
        BookSearchRequest searchRequest = new BookSearchRequest("프로그래밍", "description", "newest");
        BookDocument bookDocument = new BookDocument(2L, "자바의 정석", "자바 프로그래밍 입문서", "남궁성",
                "1234567890123", new java.util.Date(), "출판사", "35000",
                10.0f, 100L, List.of("자바", "프로그래밍"));
        PageImpl<BookDocument> searchResponse = new PageImpl<>(List.of(bookDocument), pageable, 1);

        when(bookSearchRepository.search(any(), any(), any(), any())).thenReturn(searchResponse);

        Page<BookResponse> result = bookSearchService.searchBooks(pageable, searchRequest);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).id()).isEqualTo(2L);
    }

    @Test
    @DisplayName("태그 기준 검색 테스트")
    void searchBooksByTag() {
        PageRequest pageable = PageRequest.of(0, 5);
        BookSearchRequest searchRequest = new BookSearchRequest("자바", "tag", "newest");
        BookDocument bookDocument = new BookDocument(3L, "자바의 정석", "자바 프로그래밍 입문서", "남궁성",
                "1234567890123", new java.util.Date(), "출판사", "35000",
                10.0f, 100L, List.of("자바", "프로그래밍"));
        PageImpl<BookDocument> searchResponse = new PageImpl<>(List.of(bookDocument), pageable, 1);

        when(bookSearchRepository.search(any(), any(), any(), any())).thenReturn(searchResponse);

        Page<BookResponse> result = bookSearchService.searchBooks(pageable, searchRequest);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).id()).isEqualTo(3L);
    }

    @Test
    @DisplayName("최저가 정렬 테스트")
    void searchBooksByLowestPrice() {
        PageRequest pageable = PageRequest.of(0, 5);
        BookSearchRequest searchRequest = new BookSearchRequest("자바", "title", "lowest_price");
        BookDocument bookDocument1 = new BookDocument(4L, "자바 입문", "기초 자바 입문서", "홍길동",
                "1234567890124", new java.util.Date(), "출판사A", "20000",
                10.0f, 50L, List.of("자바", "입문"));
        BookDocument bookDocument2 = new BookDocument(5L, "자바 고급", "심화 자바 프로그래밍", "김철수",
                "1234567890125", new java.util.Date(), "출판사B", "15000",
                15.0f, 30L, List.of("자바", "고급"));
        PageImpl<BookDocument> searchResponse = new PageImpl<>(List.of(bookDocument2, bookDocument1), pageable, 2);

        when(bookSearchRepository.search(any(), any(), any(), any())).thenReturn(searchResponse);

        Page<BookResponse> result = bookSearchService.searchBooks(pageable, searchRequest);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).id()).isEqualTo(5L); // 최저가가 먼저 와야 함
    }

//    @Test
//    @DisplayName("평점 기준 정렬 테스트")
//    void searchBooksByRating() {
//        PageRequest pageable = PageRequest.of(0, 5);
//        BookSearchRequest searchRequest = new BookSearchRequest("자바", "title", "rating");
//        BookDocument bookDocument = new BookDocument(6L, "자바의 정석", "자바 프로그래밍 입문서", "남궁성",
//                "1234567890123", new java.util.Date(), "출판사", "35000",
//                4.5f, 100L, List.of("자바", "프로그래밍"));
//        PageImpl<BookDocument> searchResponse = new PageImpl<>(List.of(bookDocument), pageable, 1);
//
//        when(bookSearchRepository.search(any(), any(), any(), any())).thenReturn(searchResponse);
//
//        Page<BookElasticSearchResponse> result = bookSearchService.searchBooks(pageable, searchRequest);
//        assertThat(result.getContent()).hasSize(1);
//        assertThat(result.getContent().get(0).id()).isEqualTo(6L);
//        assertThat(result.getContent().get(0).reviewRating()).isEqualTo(4.5f);
//    }

}
