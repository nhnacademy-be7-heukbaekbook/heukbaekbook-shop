//package com.nhnacademy.heukbaekbookshop.book.service.book.impl;
//
//import com.nhnacademy.heukbaekbookshop.book.domain.SearchCondition;
//import com.nhnacademy.heukbaekbookshop.book.domain.SortCondition;
//import com.nhnacademy.heukbaekbookshop.book.domain.document.BookDocument;
//import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchRequest;
//import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookResponse;
//import com.nhnacademy.heukbaekbookshop.book.repository.book.BookDocumentRepository;
//import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
//import com.nhnacademy.heukbaekbookshop.book.repository.book.BookSearchRepository;
//import com.nhnacademy.heukbaekbookshop.common.formatter.BookFormatter;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Collections;
//import java.util.Date;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//class BookSearchServiceImplTest {
//
//    private BookSearchRepository bookSearchRepository;
//    private BookRepository bookRepository;
//    private BookDocumentRepository bookDocumentRepository;
//    private BookFormatter bookFormatter;
//    private BookSearchServiceImpl bookSearchService;
//
//    @BeforeEach
//    void setUp() {
//        bookSearchRepository = Mockito.mock(BookSearchRepository.class);
//        bookRepository = Mockito.mock(BookRepository.class);
//        bookDocumentRepository = Mockito.mock(BookDocumentRepository.class);
//        bookFormatter = Mockito.mock(BookFormatter.class);
//        bookSearchService = new BookSearchServiceImpl(bookSearchRepository, bookRepository, bookDocumentRepository, bookFormatter);
//    }
//
//    @Test
//    void testSearchBooks() {
//        // given
//        BookSearchRequest searchRequest = new BookSearchRequest("test", "title", "NEWEST");
//        Pageable pageable = PageRequest.of(0, 10);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
//        Date publishedAt;
//        try {
//            publishedAt = dateFormat.parse("2024-11");
//        } catch (ParseException e) {
//            throw new RuntimeException("Invalid date format", e);
//        }
//
//        BookDocument bookDocument = new BookDocument(
//                1L, "Test Title", publishedAt, "100", 10.0,
//                "test-thumbnail-url", Collections.emptyList(), null
//        );
//        Page<BookDocument> bookDocumentPage = new PageImpl<>(Collections.singletonList(bookDocument));
//
//        when(bookSearchRepository.search(any(Pageable.class), any(String.class), any(SearchCondition.class), any(SortCondition.class)))
//                .thenReturn(bookDocumentPage);
//
//        Page<BookResponse> response = bookSearchService.searchBooks(pageable, searchRequest);
//
//        assertThat(response).isNotNull();
//        assertThat(response.getTotalElements()).isEqualTo(1);
//        assertThat(response.getContent().get(0).title()).isEqualTo("Test Title");
//    }
//}
