package com.nhnacademy.heukbaekbookshop.book.service.book;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.domain.BookCategory;
import com.nhnacademy.heukbaekbookshop.book.domain.BookStatus;
import com.nhnacademy.heukbaekbookshop.book.domain.BookTag;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookCreateRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchCondition;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookUpdateRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.*;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookAlreadyExistsException;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookNotFoundException;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookSearchException;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepository;
import com.nhnacademy.heukbaekbookshop.contributor.domain.*;
import com.nhnacademy.heukbaekbookshop.contributor.repository.ContributorRepository;
import com.nhnacademy.heukbaekbookshop.contributor.repository.PublisherRepository;
import com.nhnacademy.heukbaekbookshop.contributor.repository.RoleRepository;
import com.nhnacademy.heukbaekbookshop.image.domain.BookImage;
import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import com.nhnacademy.heukbaekbookshop.image.repository.BookImageRepository;
import com.nhnacademy.heukbaekbookshop.tag.domain.Tag;
import com.nhnacademy.heukbaekbookshop.tag.repository.TagRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

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
    private BookImageRepository bookImageRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private BookService bookService;

    @Captor
    private ArgumentCaptor<Book> bookCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Manually inject the EntityManager mock
        ReflectionTestUtils.setField(bookService, "entityManager", entityManager);

        // Mock settings
        when(publisherRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(publisherRepository.save(any(Publisher.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(contributorRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(contributorRepository.save(any(Contributor.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(roleRepository.findByRoleName(any())).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenAnswer(invocation -> {
            Role role = invocation.getArgument(0);
            role.setId(1L);
            return role;
        });
        when(categoryRepository.findByNameAndParentCategory(anyString(), any())).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(bookImageRepository.save(any(BookImage.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(bookRepository.findAllByBookSearchCondition(any(BookSearchCondition.class))).thenReturn(new ArrayList<>());
        when(bookRepository.findAllByPageable(any(Pageable.class))).thenReturn(Page.empty());
        when(bookRepository.findAllByCategoryIds(anyList(), any(Pageable.class))).thenReturn(Page.empty());
        when(bookRepository.findByBookId(anyLong())).thenReturn(Optional.empty());
        when(bookRepository.findAllByStatusNot(any(BookStatus.class), any(Pageable.class))).thenReturn(Page.empty());

        when(categoryRepository.findSubCategoryIdsByCategoryId(anyLong())).thenReturn(new ArrayList<>());

        // TagRepository Mock 설정 추가
        when(tagRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(tagRepository.save(any(Tag.class))).thenAnswer(invocation -> {
            Tag tag = invocation.getArgument(0);
            tag.setId(1L);
            return tag;
        });

        // EntityManager settings
        doNothing().when(entityManager).flush();
        doNothing().when(entityManager).remove(any());

        // Default Publisher Mock setting
        Publisher mockPublisher = new Publisher();
        mockPublisher.setName("Test Publisher");
        when(publisherRepository.findByName("Test Publisher")).thenReturn(Optional.of(mockPublisher));
    }

//    // 1. registerBook 테스트들
//    @Test
//    void registerBook_Success() {
//        // Given
//        BookCreateRequest request = createBookCreateRequest();
//
//        when(bookRepository.findByIsbn(request.isbn())).thenReturn(Optional.empty());
//        when(publisherRepository.findByName(request.publisher())).thenReturn(Optional.of(new Publisher()));
//        when(contributorRepository.findByName(anyString())).thenReturn(Optional.empty());
//        when(roleRepository.findByRoleName(any())).thenReturn(Optional.empty());
//
//        // When
//        BookCreateResponse response = bookService.registerBook(request);
//
//        // Then
//        assertNotNull(response);
//        assertEquals(request.title(), response.title());
//        verify(bookRepository, times(2)).save(any(Book.class)); // 처음 저장과 마지막 저장
//        verify(publisherRepository, never()).save(any(Publisher.class)); // 기존 출판사 사용
//        verify(contributorRepository, atLeastOnce()).findByName(anyString());
//        verify(roleRepository, atLeastOnce()).findByRoleName(any());
//        verify(bookImageRepository, times(1)).save(any(BookImage.class));
//    }
//
//    @Test
//    void registerBook_CreatesNewPublisher() {
//        // Given
//        BookCreateRequest request = createBookCreateRequest();
//        request = new BookCreateRequest(
//                request.title(),
//                request.index(),
//                request.description(),
//                request.publishedAt(),
//                request.isbn(),
//                request.imageUrl(),
//                request.isPackable(),
//                request.stock(),
//                request.standardPrice(),
//                request.discountRate(),
//                "New Publisher", // 새로운 출판사
//                request.categories(),
//                request.authors()
//        );
//
//        when(bookRepository.findByIsbn(request.isbn())).thenReturn(Optional.empty());
//        when(publisherRepository.findByName(request.publisher())).thenReturn(Optional.empty());
//
//        // When
//        BookCreateResponse response = bookService.registerBook(request);
//
//        // Then
//        assertNotNull(response);
//        assertEquals(request.title(), response.title());
//        verify(publisherRepository, times(1)).save(any(Publisher.class)); // 새로운 출판사 저장
//    }

    @Test
    void registerBook_NoCategory() {
        // Given
        BookCreateRequest request = createBookCreateRequest();
        request = new BookCreateRequest(
                request.title(),
                request.index(),
                request.description(),
                request.publishedAt(),
                request.isbn(),
                request.imageUrl(),
                request.isPackable(),
                request.stock(),
                request.standardPrice(),
                request.discountRate(),
                request.publisher(),
                null, // 카테고리 없음
                request.authors()
        );

        when(bookRepository.findByIsbn(request.isbn())).thenReturn(Optional.empty());

        // When
        BookCreateResponse response = bookService.registerBook(request);

        // Then
        assertNotNull(response);
        assertEquals(request.title(), response.title());
        verify(categoryRepository, never()).findByNameAndParentCategory(anyString(), any());
    }

//    @Test
//    void registerBook_NoAuthors() {
//        // Given
//        BookCreateRequest request = createBookCreateRequest();
//        request = new BookCreateRequest(
//                request.title(),
//                request.index(),
//                request.description(),
//                request.publishedAt(),
//                request.isbn(),
//                request.imageUrl(),
//                request.isPackable(),
//                request.stock(),
//                request.standardPrice(),
//                request.discountRate(),
//                request.publisher(),
//                request.categories(),
//                "" // authors를 빈 문자열로 설정
//        );
//
//        when(bookRepository.findByIsbn(request.isbn())).thenReturn(Optional.empty());
//
//        // When
//        BookCreateResponse response = bookService.registerBook(request);
//
//        // Then
//        assertNotNull(response);
//        assertEquals(request.title(), response.title());
//        verify(contributorRepository, never()).findByName(anyString());
//    }

//    @Test
//    void registerBook_NoImageUrl() {
//        // Given
//        BookCreateRequest request = createBookCreateRequest();
//        request = new BookCreateRequest(
//                request.title(),
//                request.index(),
//                request.description(),
//                request.publishedAt(),
//                request.isbn(),
//                null, // 이미지 URL 없음
//                request.isPackable(),
//                request.stock(),
//                request.standardPrice(),
//                request.discountRate(),
//                request.publisher(),
//                request.categories(),
//                request.authors()
//        );
//
//        when(bookRepository.findByIsbn(request.isbn())).thenReturn(Optional.empty());
//
//        // When
//        BookCreateResponse response = bookService.registerBook(request);
//
//        // Then
//        assertNotNull(response);
//        assertEquals(request.title(), response.title());
//        verify(bookImageRepository, never()).save(any(BookImage.class));
//    }

    @Test
    void registerBook_ThrowsException_WhenBookAlreadyExists() {
        // Given
        BookCreateRequest request = createBookCreateRequest();

        when(bookRepository.findByIsbn(request.isbn())).thenReturn(Optional.of(new Book()));

        // Then
        assertThrows(BookAlreadyExistsException.class, () -> bookService.registerBook(request));
    }

//    // 2. updateBook 테스트들
//    @Test
//    void updateBook_Success() {
//        // Given
//        Long bookId = 1L;
//        BookUpdateRequest request = createBookUpdateRequest();
//
//        Book book = new Book();
//        book.setId(bookId);
//        book.setPublisher(new Publisher());
//        book.setStatus(BookStatus.IN_STOCK);
//        book.setCategories(new ArrayList<>());
//        book.setContributors(new HashSet<>());
//        book.setBookImages(new HashSet<>());
//        book.setTags(new HashSet<>());
//
//        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
//        when(publisherRepository.findByName(request.publisher())).thenReturn(Optional.of(new Publisher()));
//
//        // When
//        BookUpdateResponse response = bookService.updateBook(bookId, request);
//
//        // Then
//        assertNotNull(response);
//        assertEquals(request.title(), response.title());
//        verify(bookRepository, never()).save(any(Book.class)); // updateBook 메서드에서 bookRepository.save() 호출 없음
//        verify(publisherRepository, never()).save(any(Publisher.class)); // 기존 출판사 사용
//        verify(categoryRepository, atLeastOnce()).findByNameAndParentCategory(anyString(), any());
//        verify(entityManager, times(3)).flush(); // 카테고리, 기여자, 태그 삭제 후 flush
//    }

//    @Test
//    void updateBook_CreatesNewPublisher() {
//        // Given
//        Long bookId = 1L;
//        BookUpdateRequest request = createBookUpdateRequest();
//        request = new BookUpdateRequest(
//                request.title(),
//                request.index(),
//                request.description(),
//                request.publishedAt(),
//                request.isbn(),
//                request.thumbnailImageUrl(),
//                request.detailImageUrls(),
//                request.isPackable(),
//                request.stock(),
//                request.standardPrice(),
//                request.discountRate(),
//                request.bookStatus(),
//                "New Publisher", // 새로운 출판사
//                request.categories(),
//                request.authors(),
//                request.tags()
//        );
//
//        Book book = new Book();
//        book.setId(bookId);
//        book.setPublisher(new Publisher());
//        book.setStatus(BookStatus.IN_STOCK);
//        book.setCategories(new ArrayList<>());
//        book.setContributors(new HashSet<>());
//        book.setBookImages(new HashSet<>());
//        book.setTags(new HashSet<>());
//
//        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
//        when(publisherRepository.findByName(request.publisher())).thenReturn(Optional.empty());
//
//        // When
//        BookUpdateResponse response = bookService.updateBook(bookId, request);
//
//        // Then
//        assertNotNull(response);
//        assertEquals(request.title(), response.title());
//        verify(publisherRepository, times(1)).save(any(Publisher.class)); // 새로운 출판사 저장
//    }
//
//    @Test
//    void updateBook_UpdatesCategories() {
//        // Given
//        Long bookId = 1L;
//        BookUpdateRequest request = createBookUpdateRequest();
//
//        Book book = new Book();
//        book.setId(bookId);
//        book.setPublisher(new Publisher());
//        book.setStatus(BookStatus.IN_STOCK);
//        book.setCategories(new LinkedList<>());
//        book.setContributors(new HashSet<>());
//        book.setBookImages(new HashSet<>());
//        book.setTags(new HashSet<>());
//
//        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
//        when(publisherRepository.findByName(request.publisher())).thenReturn(Optional.of(new Publisher()));
//
//        // When
//        BookUpdateResponse response = bookService.updateBook(bookId, request);
//
//        // Then
//        assertNotNull(response);
//        assertEquals(request.title(), response.title());
//        verify(categoryRepository, atLeastOnce()).findByNameAndParentCategory(anyString(), any());
//    }

    @Test
    void updateBook_ThrowsException_WhenBookNotFound() {
        // Given
        Long bookId = 1L;
        BookUpdateRequest request = createBookUpdateRequest();

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Then
        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(bookId, request));
    }

    // 3. deleteBook 테스트들
    @Test
    void deleteBook_Success() {
        // Given
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setStatus(BookStatus.IN_STOCK);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // When
        bookService.deleteBook(bookId);

        // Then
        assertEquals(BookStatus.DELETED, book.getStatus());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void deleteBook_ThrowsException_WhenBookNotFound() {
        // Given
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Then
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(bookId));
    }

    // 4. getBook 테스트들
    @Test
    void getBook_Success() {
        // Given
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Test Book");
        book.setStatus(BookStatus.IN_STOCK);
        book.setPublishedAt(Date.valueOf("2023-01-01"));
        book.setContributors(new HashSet<>());
        book.setCategories(new ArrayList<>());
        book.setTags(new HashSet<>());
        book.setBookImages(new HashSet<>());
        book.setPublisher(new Publisher());
        book.setPrice(BigDecimal.valueOf(10000)); // price 필드 설정

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // When
        BookDetailResponse response = bookService.getBook(bookId);

        // Then
        assertNotNull(response);
        assertEquals("Test Book", response.title());
    }

    @Test
    void getBook_ThrowsException_WhenBookIsDeleted() {
        // Given
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setStatus(BookStatus.DELETED);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // Then
        assertThrows(BookNotFoundException.class, () -> bookService.getBook(bookId));
    }

    // 5. increasePopularity 테스트
    @Test
    void increasePopularity_Success() {
        // Given
        Long bookId = 1L;

        // When
        bookService.increasePopularity(bookId);

        // Then
        verify(bookRepository, times(1)).increasePopularityByBookId(bookId);
    }

    // 6. searchBook 테스트들
    @Test
    void searchBook_Success() {
        // Given
        String title = "Test Title";
        String aladinApiKey = "test-api-key";
        ReflectionTestUtils.setField(bookService, "aladinApiKey", aladinApiKey);
        String url = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx" +
                "?ttbkey=" + aladinApiKey +
                "&Query=" + title +
                "&QueryType=Title" +
                "&MaxResults=10" +
                "&start=1" +
                "&SearchTarget=Book" +
                "&output=js" +
                "&Version=20131101";

        BookSearchApiResponse apiResponse = new BookSearchApiResponse();
        apiResponse.setItems(List.of(new BookSearchApiResponse.Item()));

        when(restTemplate.getForObject(url, BookSearchApiResponse.class)).thenReturn(apiResponse);

        // When
        List<BookSearchResponse> responses = bookService.searchBook(title);

        // Then
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
    }

    @Test
    void searchBook_ThrowsException_WhenApiCallFails() {
        // Given
        String title = "Test Title";
        String aladinApiKey = "test-api-key";
        ReflectionTestUtils.setField(bookService, "aladinApiKey", aladinApiKey);
        String url = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx" +
                "?ttbkey=" + aladinApiKey +
                "&Query=" + title +
                "&QueryType=Title" +
                "&MaxResults=10" +
                "&start=1" +
                "&SearchTarget=Book" +
                "&output=js" +
                "&Version=20131101";

        when(restTemplate.getForObject(url, BookSearchApiResponse.class))
                .thenThrow(new RestClientException("API call failed"));

        // Then
        assertThrows(BookSearchException.class, () -> bookService.searchBook(title));
    }

    // 유틸리티 메서드들
    private BookCreateRequest createBookCreateRequest() {
        return new BookCreateRequest(
                "Test Title",
                "Index123",
                "Test Description",
                "2023-01-01",
                "9781234567890",
                "http://example.com/image.jpg",
                true,
                10,
                10000,
                BigDecimal.valueOf(0.1),
                "Test Publisher",
                "Category > SubCategory",
                "Author Name"
        );
    }

    private BookUpdateRequest createBookUpdateRequest() {
        return new BookUpdateRequest(
                "Updated Title",
                "Updated Index",
                "Updated Description",
                "2023-01-02",
                "9789876543210",
                "http://example.com/thumbnail.jpg",
                List.of("http://example.com/detail1.jpg"),
                true,
                15,
                20000,
                BigDecimal.valueOf(0.2),
                "IN_STOCK",
                "Test Publisher",
                List.of("Category1 > SubCategory1", "Category2 > SubCategory2"),
                "Updated Author",
                List.of("Tag1", "Tag2")
        );
    }

    @Test
    void getBooksSummary_Success() {
        // Given
        List<Long> bookIds = Arrays.asList(1L, 2L, 3L);

        Book book1 = createTestBook(1L, "Book 1");
        Book book2 = createTestBook(2L, "Book 2");
        Book book3 = createTestBook(3L, "Book 3");

        List<Book> books = Arrays.asList(book1, book2, book3);

        when(bookRepository.findAllByBookSearchCondition(any(BookSearchCondition.class))).thenReturn(books);

        // When
        List<BookSummaryResponse> responses = bookService.getBooksSummary(bookIds);

        // Then
        assertNotNull(responses);
        assertEquals(3, responses.size());
        assertEquals("Book 1", responses.get(0).title());
        assertEquals("Book 2", responses.get(1).title());
        assertEquals("Book 3", responses.get(2).title());
    }

    @Test
    void getBooks_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Book book1 = createTestBook(1L, "Book 1");
        Book book2 = createTestBook(2L, "Book 2");

        Page<Book> bookPage = new PageImpl<>(Arrays.asList(book1, book2));

        when(bookRepository.findAllByPageable(pageable)).thenReturn(bookPage);

        // When
        Page<BookResponse> responses = bookService.getBooks(pageable);

        // Then
        assertNotNull(responses);
        assertEquals(2, responses.getContent().size());
        assertEquals("Book 1", responses.getContent().get(0).title());
        assertEquals("Book 2", responses.getContent().get(1).title());
    }

    @Test
    void getBooksByCategoryId_Success() {
        // Given
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        List<Long> categoryIds = Arrays.asList(1L, 2L);
        when(categoryRepository.findSubCategoryIdsByCategoryId(categoryId)).thenReturn(categoryIds);

        Book book1 = createTestBook(1L, "Book 1");
        Book book2 = createTestBook(2L, "Book 2");

        Page<Book> bookPage = new PageImpl<>(Arrays.asList(book1, book2));
        when(bookRepository.findAllByCategoryIds(categoryIds, pageable)).thenReturn(bookPage);

        // When
        Page<BookResponse> responses = bookService.getBooksByCategoryId(categoryId, pageable);

        // Then
        assertNotNull(responses);
        assertEquals(2, responses.getContent().size());
        assertEquals("Book 1", responses.getContent().get(0).title());
        assertEquals("Book 2", responses.getContent().get(1).title());
    }

    @Test
    void getBookDetail_Success() {
        // Given
        Long bookId = 1L;
        Book book = createTestBook(bookId, "Test Book");

        when(bookRepository.findByBookId(bookId)).thenReturn(Optional.of(book));

        // When
        BookViewResponse response = bookService.getBookDetail(bookId);

        // Then
        assertNotNull(response);
        assertEquals("Test Book", response.title());
        assertEquals(bookId, response.id());
    }

    @Test
    void getBookDetail_BookNotFound() {
        // Given
        Long bookId = 1L;

        when(bookRepository.findByBookId(bookId)).thenReturn(Optional.empty());

        // Then
        assertThrows(BookNotFoundException.class, () -> bookService.getBookDetail(bookId));
    }

    @Test
    void getBooksDetail_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Book book1 = createDetailedTestBook(1L, "Book 1");
        Book book2 = createDetailedTestBook(2L, "Book 2");

        Page<Book> bookPage = new PageImpl<>(Arrays.asList(book1, book2));

        when(bookRepository.findAllByStatusNot(BookStatus.DELETED, pageable)).thenReturn(bookPage);

        // When
        Page<BookDetailResponse> responses = bookService.getBooksDetail(pageable);

        // Then
        assertNotNull(responses);
        assertEquals(2, responses.getContent().size());

        BookDetailResponse response1 = responses.getContent().get(0);
        assertEquals("Book 1", response1.title());
        assertEquals("Test Publisher", response1.publisher());
        assertEquals(List.of("Category1>SubCategory1"), response1.categories());
        assertEquals(List.of("Author 1"), response1.authors());
        assertEquals(Arrays.asList("Tag1", "Tag2"), response1.tags());
        assertEquals("http://example.com/thumbnail1.jpg", response1.thumbnailImageUrl());
        assertEquals(List.of("http://example.com/detail1.jpg"), response1.detailImageUrls());

    }

    private Book createTestBook(Long id, String title) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setStatus(BookStatus.IN_STOCK);
        book.setPublishedAt(Date.valueOf("2023-01-01"));
        book.setContributors(new HashSet<>());
        book.setCategories(new ArrayList<>());
        book.setTags(new HashSet<>());
        book.setBookImages(new HashSet<>());
        book.setPublisher(new Publisher());
        book.setPrice(BigDecimal.valueOf(10000));
        book.setDiscountRate(BigDecimal.valueOf(0.1));
        book.setPopularity(100);

        return book;
    }

    private Book createDetailedTestBook(Long id, String title) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setIndex("Index " + id);
        book.setDescription("Description for " + title);
        book.setPublishedAt(Date.valueOf("2023-01-01"));
        book.setIsbn("ISBN-" + id);
        book.setPackable(true);
        book.setStock(100);
        book.setPrice(BigDecimal.valueOf(20000));
        book.setDiscountRate(BigDecimal.valueOf(0.1));
        book.setStatus(BookStatus.IN_STOCK);
        book.setPopularity(100);

        // Publisher 설정
        Publisher publisher = new Publisher();
        publisher.setId(1L);
        publisher.setName("Test Publisher");
        book.setPublisher(publisher);

        // Category 설정
        Category subCategory = new Category();
        subCategory.setId(2L);
        subCategory.setName("SubCategory1");

        Category mainCategory = new Category();
        mainCategory.setId(1L);
        mainCategory.setName("Category1");
        subCategory.setParentCategory(mainCategory);

        BookCategory bookCategory = new BookCategory(book, subCategory);

        book.setCategories(List.of(bookCategory));

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

        book.setContributors(new HashSet<>(Arrays.asList(bookContributor)));

        // Tag 설정
        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("Tag1");

        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("Tag2");

        BookTag bookTag1 = new BookTag();
        bookTag1.setBook(book);
        bookTag1.setTag(tag1);

        BookTag bookTag2 = new BookTag();
        bookTag2.setBook(book);
        bookTag2.setTag(tag2);

        book.setTags(new HashSet<>(Arrays.asList(bookTag1, bookTag2)));

        // BookImage 설정
        BookImage thumbnailImage = new BookImage();
        thumbnailImage.setId(1L);
        thumbnailImage.setUrl("http://example.com/thumbnail" + id + ".jpg");
        thumbnailImage.setType(ImageType.THUMBNAIL);

        BookImage detailImage = new BookImage();
        detailImage.setId(2L);
        detailImage.setUrl("http://example.com/detail" + id + ".jpg");
        detailImage.setType(ImageType.DETAIL);

        book.setBookImages(new HashSet<>(Arrays.asList(thumbnailImage, detailImage)));

        return book;
    }

}
