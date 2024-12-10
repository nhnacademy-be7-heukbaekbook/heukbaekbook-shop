package com.nhnacademy.heukbaekbookshop.book.service.book.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.*;
import com.nhnacademy.heukbaekbookshop.book.domain.document.BookDocument;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookResponse;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookDocumentRepository;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookSearchRepository;
import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepository;
import com.nhnacademy.heukbaekbookshop.common.util.IndexNameProvider;
import com.nhnacademy.heukbaekbookshop.contributor.domain.*;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorSummaryResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherSummaryResponse;
import com.nhnacademy.heukbaekbookshop.image.domain.BookImage;
import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import com.nhnacademy.heukbaekbookshop.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookSearchServiceImplTest {

    @Mock
    private BookSearchRepository bookSearchRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookDocumentRepository bookDocumentRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private IndexNameProvider indexNameProvider;

    @Mock
    private ElasticsearchOperations elasticsearchOperations;

    @InjectMocks
    private BookSearchServiceImpl bookSearchService;

    @Captor
    private ArgumentCaptor<BookDocument> bookDocumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // searchBooks 메서드에 대한 테스트
    @Test
    void searchBooks_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        BookSearchRequest searchRequest = new BookSearchRequest(
                "Test Keyword",
                "title",
                "popularity",
                null
        );

        String indexName = "test-index";
        when(indexNameProvider.resolveIndexName()).thenReturn(indexName);

        BookDocument bookDocument = createTestBookDocument(1L, "Test Book");
        Page<BookDocument> bookDocumentPage = new PageImpl<>(List.of(bookDocument));

        when(bookSearchRepository.search(
                eq(indexName),
                eq(pageable),
                eq("Test Keyword"),
                eq(SearchCondition.TITLE),
                eq(SortCondition.POPULARITY),
                eq(null)
        )).thenReturn(bookDocumentPage);

        Book book = createTestBook(1L, "Test Book");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // When
        Page<BookResponse> responses = bookSearchService.searchBooks(pageable, searchRequest);

        // Then
        assertNotNull(responses);
        assertEquals(1, responses.getContent().size());

        BookResponse response = responses.getContent().get(0);
        assertEquals("Test Book", response.title());
        assertEquals("Test Publisher", response.publisher().name());
    }

    @Test
    void searchBooks_BookNotFound() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        BookSearchRequest searchRequest = new BookSearchRequest(
                "Test Keyword",
                "title",
                "popularity",
                null
        );

        String indexName = "test-index";
        when(indexNameProvider.resolveIndexName()).thenReturn(indexName);

        BookDocument bookDocument = createTestBookDocument(1L, "Test Book");
        Page<BookDocument> bookDocumentPage = new PageImpl<>(List.of(bookDocument));

        when(bookSearchRepository.search(
                eq(indexName),
                eq(pageable),
                eq("Test Keyword"),
                eq(SearchCondition.TITLE),
                eq(SortCondition.POPULARITY),
                eq(null)
        )).thenReturn(bookDocumentPage);

        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookSearchService.searchBooks(pageable, searchRequest);
        });
        assertTrue(exception.getMessage().contains("Book not found with ID: 1"));
    }

    // 필요한 헬퍼 메서드들
    private BookDocument createTestBookDocument(Long id, String title) {
        return new BookDocument(
                id,
                title,
                Date.valueOf("2023-01-01"),
                10000,
                0.1,
                "http://example.com/image.jpg",
                List.of("Author 1"),
                "Test Description",
                List.of(new ContributorSummaryResponse(1L, "Author 1")),
                new PublisherSummaryResponse(1L, "Test Publisher"),
                100L,
                List.of(1L, 2L),
                10,
                4.5f
        );
    }

    private Book createTestBook(Long id, String title) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setPublishedAt(Date.valueOf("2023-01-01"));
        book.setPrice(BigDecimal.valueOf(10000));
        book.setDiscountRate(BigDecimal.valueOf(0.1));
        book.setStatus(BookStatus.IN_STOCK);
        book.setPopularity(100);

        // Publisher 설정
        Publisher publisher = new Publisher();
        publisher.setId(1L);
        publisher.setName("Test Publisher");
        book.setPublisher(publisher);

        // BookImage 설정
        BookImage thumbnailImage = new BookImage();
        thumbnailImage.setId(1L);
        thumbnailImage.setUrl("http://example.com/image.jpg");
        thumbnailImage.setType(ImageType.THUMBNAIL);

        book.setBookImages(new HashSet<>(List.of(thumbnailImage)));

        // Contributor 설정
        Contributor contributor = new Contributor();
        contributor.setId(1L);
        contributor.setName("Author 1");

        Role role = new Role();
        role.setId(1L);
        role.setRoleName(ContributorRole.AUTHOR);

        BookContributor bookContributor = new BookContributor();
        bookContributor.setBook(book);
        bookContributor.setContributor(contributor);
        bookContributor.setRole(role);

        book.setContributors(new HashSet<>(List.of(bookContributor)));

        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        // Categories 설정
        BookCategory bookCategory = new BookCategory(book, category);

        book.setCategories(List.of(bookCategory));

        ReflectionTestUtils.setField(book, "id", id);

        return book;
    }
}
