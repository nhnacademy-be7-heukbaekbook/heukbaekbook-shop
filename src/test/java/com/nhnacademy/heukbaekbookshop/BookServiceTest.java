package com.nhnacademy.heukbaekbookshop;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.domain.BookCategory;
import com.nhnacademy.heukbaekbookshop.book.domain.BookStatus;
import com.nhnacademy.heukbaekbookshop.book.dto.request.BookCreateRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.request.BookSearchRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.request.BookUpdateRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.response.*;
import com.nhnacademy.heukbaekbookshop.book.exception.BookAlreadyExistsException;
import com.nhnacademy.heukbaekbookshop.book.exception.BookNotFoundException;
import com.nhnacademy.heukbaekbookshop.book.exception.BookSearchException;
import com.nhnacademy.heukbaekbookshop.book.repository.BookRepository;
import com.nhnacademy.heukbaekbookshop.book.service.BookService;
import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepository;
import com.nhnacademy.heukbaekbookshop.contributor.domain.*;
import com.nhnacademy.heukbaekbookshop.contributor.repository.ContributorRepository;
import com.nhnacademy.heukbaekbookshop.contributor.repository.PublisherRepository;
import com.nhnacademy.heukbaekbookshop.contributor.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.Null;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ContributorRepository contributorRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Set the aladinApiKey field
        ReflectionTestUtils.setField(bookService, "aladinApiKey", "testApiKey");
    }

    // Test for searchBook method
    @Test
    public void testSearchBook_Success() {
        // Given
        BookSearchRequest request = new BookSearchRequest("Test Title");
        String url = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx" +
                "?ttbkey=testApiKey" +
                "&Query=Test Title" +
                "&QueryType=Title" +
                "&MaxResults=10" +
                "&start=1" +
                "&SearchTarget=Book" +
                "&output=js" +
                "&Version=20131101";

        BookSearchApiResponse.Item item = new BookSearchApiResponse.Item();
        item.setTitle("Test Book");
        item.setPubDate("2023-01-01");
        item.setCover("cover_url");
        item.setDescription("A test book");
        item.setCategory("Test Category");
        item.setAuthor("Test Author");
        item.setPublisher("Test Publisher");
        item.setIsbn("1234567890");
        item.setStandardPrice(10000);
        item.setSalesPrice(9000);

        BookSearchApiResponse apiResponse = new BookSearchApiResponse();
        apiResponse.setItems(Arrays.asList(item));

        when(restTemplate.getForObject(eq(url), eq(BookSearchApiResponse.class))).thenReturn(apiResponse);

        // When
        List<BookSearchResponse> responses = bookService.searchBook(request);

        // Then
        assertNotNull(responses);
        assertEquals(1, responses.size());
        BookSearchResponse response = responses.get(0);
        assertEquals("Test Book", response.title());
        assertEquals("cover_url", response.cover());
        assertEquals("A test book", response.description());
        assertEquals("Test Category", response.category());
        assertEquals("Test Author", response.author());
        assertEquals("Test Publisher", response.publisher());
        assertEquals(LocalDate.of(2023, 1, 1), response.pubDate());
        assertEquals("1234567890", response.isbn());
        assertEquals(10000, response.standardPrice());
        assertEquals(9000, response.salesPrice());
    }

    @Test
    public void testSearchBook_NullApiResponse() {
        // Given
        BookSearchRequest request = new BookSearchRequest("Test Title");
        String url = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx" +
                "?ttbkey=testApiKey" +
                "&Query=Test Title" +
                "&QueryType=Title" +
                "&MaxResults=10" +
                "&start=1" +
                "&SearchTarget=Book" +
                "&output=js" +
                "&Version=20131101";

        when(restTemplate.getForObject(eq(url), eq(BookSearchApiResponse.class))).thenReturn(null);

        // When
        List<BookSearchResponse> responses = bookService.searchBook(request);

        // Then
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    public void testSearchBook_RestClientException() {
        // Given
        BookSearchRequest request = new BookSearchRequest("Test Title");
        String url = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx" +
                "?ttbkey=testApiKey" +
                "&Query=Test Title" +
                "&QueryType=Title" +
                "&MaxResults=10" +
                "&start=1" +
                "&SearchTarget=Book" +
                "&output=js" +
                "&Version=20131101";

        when(restTemplate.getForObject(eq(url), eq(BookSearchApiResponse.class)))
                .thenThrow(new RestClientException("Error"));

        // When & Then
        assertThrows(BookSearchException.class, () -> bookService.searchBook(request));
    }

    @Test
    public void testSearchBook_InvalidPubDate() {
        // Given
        BookSearchRequest request = new BookSearchRequest("Test Title");
        String url = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx" +
                "?ttbkey=testApiKey" +
                "&Query=Test Title" +
                "&QueryType=Title" +
                "&MaxResults=10" +
                "&start=1" +
                "&SearchTarget=Book" +
                "&output=js" +
                "&Version=20131101";

        BookSearchApiResponse.Item item = new BookSearchApiResponse.Item();
        item.setTitle("Test Book");
        item.setPubDate("Invalid Date");
        item.setCover("cover_url");
        item.setDescription("A test book");
        item.setCategory("Test Category");
        item.setAuthor("Test Author");
        item.setPublisher("Test Publisher");
        item.setIsbn("1234567890");
        item.setStandardPrice(10000);
        item.setSalesPrice(9000);

        BookSearchApiResponse apiResponse = new BookSearchApiResponse();
        apiResponse.setItems(Arrays.asList(item));

        when(restTemplate.getForObject(eq(url), eq(BookSearchApiResponse.class))).thenReturn(apiResponse);

        // When
        List<BookSearchResponse> responses = bookService.searchBook(request);

        // Then
        assertNotNull(responses);
        assertEquals(1, responses.size());
        BookSearchResponse response = responses.get(0);
        assertEquals(LocalDate.now(), response.pubDate());
    }

    @Test
    public void testRegisterBook_BookAlreadyExists() {
        // Given
        BookCreateRequest request = new BookCreateRequest(
                "Test Book",
                "Test Index",
                "Test Description",
                "2023-01-01",
                "1234567890",
                true,
                10,
                10000,
                0.1f,
                "Test Publisher",
                Arrays.asList("Category1", "Category2"),
                Arrays.asList("Author1", "Author2")
        );

        when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.of(new Book()));

        // When & Then
        assertThrows(BookAlreadyExistsException.class, () -> bookService.registerBook(request));

        verify(bookRepository).findByIsbn("1234567890");
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void testRegisterBook_PublisherCreated() {
        // Given
        BookCreateRequest request = new BookCreateRequest(
                "Test Book",
                "Test Index",
                "Test Description",
                "2023-01-01",
                "1234567890",
                true,
                10,
                10000,
                0.1f,
                "New Publisher",
                Arrays.asList("Category1", "Category2"),
                Arrays.asList("Author1", "Author2")
        );

        when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.empty());

        when(publisherRepository.findByName("New Publisher")).thenReturn(Optional.empty());
        when(publisherRepository.save(any(Publisher.class))).thenAnswer(invocation -> {
            Publisher publisher = invocation.getArgument(0);
            publisher.setId(1L);
            return publisher;
        });

        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book book = invocation.getArgument(0);
            book.setId(1L);
            return book;
        });

        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Category1");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Category2");

        when(categoryRepository.findByName("Category1")).thenReturn(Optional.of(category1));
        when(categoryRepository.findByName("Category2")).thenReturn(Optional.of(category2));

        Contributor author1 = new Contributor();
        author1.setId(1L);
        author1.setName("Author1");

        Contributor author2 = new Contributor();
        author2.setId(2L);
        author2.setName("Author2");

        when(contributorRepository.findByName("Author1")).thenReturn(Optional.of(author1));
        when(contributorRepository.findByName("Author2")).thenReturn(Optional.of(author2));

        Role authorRole = new Role();
        authorRole.setId(1L);
        authorRole.setRoleName(ContributorRole.AUTHOR);

        when(roleRepository.findByRoleName(ContributorRole.AUTHOR)).thenReturn(Optional.of(authorRole));

        // When
        BookCreateResponse response = bookService.registerBook(request);

        // Then
        assertNotNull(response);
        assertEquals("Test Book", response.title());
        assertEquals("New Publisher", response.publisher());

        verify(publisherRepository).findByName("New Publisher");
        verify(publisherRepository).save(any(Publisher.class));
    }

    @Test
    public void testUpdateBook_BookNotFound() {
        // Given
        Long bookId = 1L;
        BookUpdateRequest request = new BookUpdateRequest(
                "Updated Book",
                "Updated Index",
                "Updated Description",
                "2023-01-01",
                "1234567890",
                true,
                20,
                15000,
                0.2f,
                "IN_STOCK",
                "Updated Publisher",
                Arrays.asList("UpdatedCategory1", "UpdatedCategory2"),
                Arrays.asList("UpdatedAuthor1", "UpdatedAuthor2")
        );

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(bookId, request));

        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void testUpdateBook_BookIdNull() {
        // Given
        Long bookId = 1L;
        BookUpdateRequest request = new BookUpdateRequest(
                "Updated Book",
                "Updated Index",
                "Updated Description",
                "2023-01-01",
                "1234567890",
                true,
                20,
                15000,
                0.2f,
                "IN_STOCK",
                "Updated Publisher",
                Arrays.asList("UpdatedCategory1", "UpdatedCategory2"),
                Arrays.asList("UpdatedAuthor1", "UpdatedAuthor2")
        );

        Book book = new Book();
        book.setCategories(new HashSet<>());
        book.setContributors(new HashSet<>());

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NullPointerException.class, () -> bookService.updateBook(bookId, request));
    }

    @Test
    public void testUpdateBook_CategoryIdNull() {
        // Given
        Long bookId = 1L;
        BookUpdateRequest request = new BookUpdateRequest(
                "Updated Book",
                "Updated Index",
                "Updated Description",
                "2023-01-01",
                "1234567890",
                true,
                20,
                15000,
                0.2f,
                "IN_STOCK",
                "Updated Publisher",
                Arrays.asList("UpdatedCategory1", "UpdatedCategory2"),
                Arrays.asList("UpdatedAuthor1", "UpdatedAuthor2")
        );

        Book book = new Book();
        book.setId(bookId);
        book.setCategories(new HashSet<>());
        book.setContributors(new HashSet<>());

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Category category = new Category();
        category.setName("UpdatedCategory1");
        // category ID is null

        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));

        // When & Then
        assertThrows(NullPointerException.class, () -> bookService.updateBook(bookId, request));
    }

    // Test for deleteBook method
    @Test
    public void testDeleteBook_Success() {
        // Given
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setStatus(BookStatus.IN_STOCK);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        when(bookRepository.save(book)).thenReturn(book);

        // When
        BookDeleteResponse response = bookService.deleteBook(bookId);

        // Then
        assertNotNull(response);
        assertEquals("Book deleted successfully.", response.message());
        assertEquals(BookStatus.DELETED, book.getStatus());

        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(book);
    }

    @Test
    public void testDeleteBook_BookNotFound() {
        // Given
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(bookId));

        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void testGetBook_BookNotFound() {
        // Given
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BookNotFoundException.class, () -> bookService.getBook(bookId));

        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }
}
