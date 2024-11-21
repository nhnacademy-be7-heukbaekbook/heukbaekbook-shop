//package com.nhnacademy.heukbaekbookshop.book.service.book.impl;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//import com.nhnacademy.heukbaekbookshop.book.domain.Book;
//import com.nhnacademy.heukbaekbookshop.book.domain.SearchCondition;
//import com.nhnacademy.heukbaekbookshop.book.domain.SortCondition;
//import com.nhnacademy.heukbaekbookshop.book.domain.document.BookDocument;
//import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchRequest;
//import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookResponse;
//import com.nhnacademy.heukbaekbookshop.book.repository.book.BookDocumentRepository;
//import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
//import com.nhnacademy.heukbaekbookshop.book.repository.book.BookSearchRepository;
//import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepository;
//import com.nhnacademy.heukbaekbookshop.common.formatter.BookFormatter;
//import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorSummaryResponse;
//import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherSummaryResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.math.BigDecimal;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//class BookSearchServiceImplTest {
//
//    private BookSearchRepository bookSearchRepository;
//    private BookRepository bookRepository;
//    private BookDocumentRepository bookDocumentRepository;
//    private BookFormatter bookFormatter;
//    private BookSearchServiceImpl bookSearchService;
//    private CategoryRepository categoryRepository;
//
//    private Book book;
//
//
//
//    @BeforeEach
//    void setUp() {
//        bookSearchRepository = Mockito.mock(BookSearchRepository.class);
//        bookRepository = Mockito.mock(BookRepository.class);
//        book = Mockito.mock(Book.class);
//        bookDocumentRepository = Mockito.mock(BookDocumentRepository.class);
//        bookFormatter = Mockito.mock(BookFormatter.class);
//        bookSearchService = new BookSearchServiceImpl(bookSearchRepository, bookRepository, bookDocumentRepository, categoryRepository, bookFormatter);
//    }
//
//    @Test
//    void testSearchBooks() throws ParseException {
//        // given
//        BookSearchRequest searchRequest = new BookSearchRequest("test", "title", "NEWEST", null);
//        Pageable pageable = PageRequest.of(0, 10);
//
//        BookDocument bookDocument = new BookDocument(
//                1L, "Test Title", new Date(), 100, 10.0, "test-thumbnail-url",
//                Arrays.asList("Author1", "Author2"), "Test Description",
//                List.of(new ContributorSummaryResponse(1L, "Contributor Name")),
//                new PublisherSummaryResponse(1L, "Publisher Name"),
//                1L, null
//        );
//
//        Book book = new Book();
//        book.setId(1L);
//        book.setTitle("Test Title");
//        book.setPrice(BigDecimal.valueOf(100));
//        book.setDiscountRate(10.0f);
//
//        // SimpleDateFormat으로 String -> Date 변환
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date parsedDate = dateFormat.parse(bookFormatter.formatDate(new Date()));
//        book.setPublishedAt(parsedDate); // Date 타입으로 설정
//
//        book.setPopularity(1L);
//
//        when(bookSearchRepository.search(any(Pageable.class), any(String.class), any(SearchCondition.class), any(SortCondition.class), any()))
//                .thenReturn(new PageImpl<>(Collections.singletonList(bookDocument)));
//        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
//
//        // when
//        Page<BookResponse> response = bookSearchService.searchBooks(pageable, searchRequest);
//
//        // then
//        assertThat(response).isNotNull();
//        assertThat(response.getTotalElements()).isEqualTo(1);
//        assertThat(response.getContent().get(0).title()).isEqualTo("Test Title");
//    }
//}