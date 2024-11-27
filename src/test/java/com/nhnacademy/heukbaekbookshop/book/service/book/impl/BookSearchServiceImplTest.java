//package com.nhnacademy.heukbaekbookshop.book.service.book.impl;
//
//import com.nhnacademy.heukbaekbookshop.book.domain.*;
//import com.nhnacademy.heukbaekbookshop.book.domain.document.BookDocument;
//import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchRequest;
//import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookResponse;
//import com.nhnacademy.heukbaekbookshop.book.repository.book.BookDocumentRepository;
//import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
//import com.nhnacademy.heukbaekbookshop.book.repository.book.BookSearchRepository;
//import com.nhnacademy.heukbaekbookshop.book.service.book.BookSearchService;
//import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepository;
//import com.nhnacademy.heukbaekbookshop.common.formatter.BookFormatter;
//import com.nhnacademy.heukbaekbookshop.common.service.CommonService;
//import com.nhnacademy.heukbaekbookshop.contributor.domain.BookContributor;
//import com.nhnacademy.heukbaekbookshop.contributor.domain.Contributor;
//import com.nhnacademy.heukbaekbookshop.contributor.domain.ContributorRole;
//import com.nhnacademy.heukbaekbookshop.contributor.domain.Publisher;
//import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorSummaryResponse;
//import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherSummaryResponse;
//import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//
//import java.math.BigDecimal;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.sql.Date;
//
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class BookSearchServiceImplTest {
//
//    @InjectMocks
//    private BookSearchServiceImpl bookSearchService;
//
//    @Mock
//    private BookSearchRepository bookSearchRepository;
//
//    @Mock
//    private BookRepository bookRepository;
//
//    @Mock
//    private BookDocumentRepository bookDocumentRepository;
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @Mock
//    private BookFormatter bookFormatter;
//
//    @Mock
//    private CommonService commonService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    Date publishedAt = Date.valueOf("2023-11-24");
//
//    @Test
//    void testSearchBooks() {
//        // Given
//        Pageable pageable = Pageable.ofSize(10).withPage(0);
//        BookSearchRequest searchRequest = new BookSearchRequest("keyword", "ALL", "POPULARITY", 1L);
//
//        BookDocument mockDocument = new BookDocument(
//                1L,
//                "Book Title",
//                publishedAt,
//                8000,
//                0.2,
//                "thumbnailUrl",
//                List.of("Author 1", "Author 2"),
//                "description",
//                List.of(),
//                null,
//                100L,
//                List.of(1L)
//        );
//        Page<BookDocument> mockDocuments = new PageImpl<>(List.of(mockDocument));
//
//        Book mockBook = new Book();
//        mockBook.setId(1L);
//        mockBook.setTitle("Book Title");
//        mockBook.setPublishedAt(new java.sql.Date(publishedAt.getTime()));
//        mockBook.setPrice(BigDecimal.valueOf(10000));
//        mockBook.setDiscountRate(BigDecimal.valueOf(20));
//        mockBook.setPopularity(100L);
//        mockBook.setPackable(true);
//        mockBook.setBookImages(Set.of());
//        mockBook.setPublisher(new Publisher(1L, "Publisher Name"));
//
//        when(bookSearchRepository.search(any(Pageable.class), anyString(), any(SearchCondition.class), any(SortCondition.class), anyLong()))
//                .thenReturn(mockDocuments);
//        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));
//        when(commonService.getSalePrice(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(8000));
//        when(commonService.formatPrice(any(BigDecimal.class))).thenReturn("8,000원");
//        when(bookFormatter.formatDate(any())).thenReturn("2024년 01월");
//
//        // When
//        Page<BookResponse> result = bookSearchService.searchBooks(pageable, searchRequest);
//
//        // Then
//        assertThat(result).isNotNull();
//        assertThat(result.getTotalElements()).isEqualTo(1);
//
//        BookResponse bookResponse = result.getContent().get(0);
//        assertThat(bookResponse.id()).isEqualTo(1L);
//        assertThat(bookResponse.title()).isEqualTo("Book Title");
//        assertThat(bookResponse.salePrice()).isEqualTo("8,000원");
//        assertThat(bookResponse.thumbnailUrl()).isEqualTo("no-image");
//
//        verify(bookSearchRepository, times(1)).search(any(Pageable.class), anyString(), any(SearchCondition.class), any(SortCondition.class), anyLong());
//        verify(bookRepository, times(1)).findById(1L);
//        verify(commonService, times(1)).getSalePrice(any(BigDecimal.class), any(BigDecimal.class));
//        verify(bookFormatter, times(1)).formatDate(any());
//    }
//
//    @Test
//    void testUpdateBookIndex() throws ParseException {
//        // Given
//        List<Book> allBooks = List.of(
//                new Book(1L, "Title 1", "Index 1", "Description 1", publishedAt,
//                        "1234567890123", true, 100, BigDecimal.valueOf(20000), BigDecimal.valueOf(10),
//                        100L, BookStatus.IN_STOCK, new Publisher(), new HashSet<>(), new HashSet<>(), new HashSet<>(),
//                        new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>()),
//                new Book(2L, "Title 2", "Index 2", "Description 2", publishedAt,
//                        "1234567890124", true, 50, BigDecimal.valueOf(30000), BigDecimal.valueOf(15),
//                        200L, BookStatus.IN_STOCK, new Publisher(), new HashSet<>(), new HashSet<>(), new HashSet<>(),
//                        new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>())
//        );
//
//        List<Book> deletedBooks = List.of(
//                new Book(3L, "Title 3", "Index 3", "Description 3", publishedAt,
//                        "1234567890125", true, 0, BigDecimal.valueOf(15000), BigDecimal.valueOf(5),
//                        50L, BookStatus.DELETED, new Publisher(), new HashSet<>(), new HashSet<>(), new HashSet<>(),
//                        new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>())
//        );
//
//        when(bookRepository.findAllByStatusNot(BookStatus.DELETED)).thenReturn(allBooks);
//        when(bookRepository.findAllByStatus(BookStatus.DELETED)).thenReturn(deletedBooks);
//
//        doNothing().when(bookDocumentRepository).deleteAllById(anyList());
//        doReturn(List.of(1L, 2L, 3L)).when(categoryRepository).findParentCategoryIdsByCategoryIds(anySet());
//
//        // When
//        bookSearchService.updateBookIndex();
//
//        // Then
//        verify(bookRepository, times(1)).findAllByStatusNot(BookStatus.DELETED);
//        verify(bookRepository, times(1)).findAllByStatus(BookStatus.DELETED);
//        verify(bookDocumentRepository, times(1)).deleteAllById(List.of(3L));
//        verify(bookDocumentRepository, times(1)).saveAll(anyList());
//    }
//}
