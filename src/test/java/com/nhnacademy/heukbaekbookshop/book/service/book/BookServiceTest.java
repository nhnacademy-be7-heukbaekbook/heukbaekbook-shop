package com.nhnacademy.heukbaekbookshop.book.service.book;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.domain.BookStatus;
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
import com.nhnacademy.heukbaekbookshop.contributor.domain.Contributor;
import com.nhnacademy.heukbaekbookshop.contributor.domain.Publisher;
import com.nhnacademy.heukbaekbookshop.contributor.domain.Role;
import com.nhnacademy.heukbaekbookshop.contributor.repository.ContributorRepository;
import com.nhnacademy.heukbaekbookshop.contributor.repository.PublisherRepository;
import com.nhnacademy.heukbaekbookshop.contributor.repository.RoleRepository;
import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import com.nhnacademy.heukbaekbookshop.image.repository.BookImageRepository;
import com.nhnacademy.heukbaekbookshop.tag.domain.Tag;
import com.nhnacademy.heukbaekbookshop.tag.repository.TagRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doNothing().when(entityManager).flush();
        doNothing().when(entityManager).remove(any());

        ReflectionTestUtils.setField(bookService, "entityManager", entityManager);

        // 모든 categoryRepository.save() 호출에 대해 안전하게 처리
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category c = invocation.getArgument(0);
            if (c == null) {
                c = new Category();
            }
            if (c.getName() == null || c.getName().trim().isEmpty()) {
                c.setName("defaultCategoryName");
            }
            c.setId(new Random().nextLong());
            return c;
        });
    }

    @Test
    @DisplayName("알라딘 검색 API 성공 테스트")
    void testSearchBookSuccess() {
        BookSearchApiResponse.Item item = new BookSearchApiResponse.Item();
        item.setTitle("Java Programming");
        item.setAuthor("John Doe");
        item.setCategory("IT");
        item.setCover("http://example.com/cover.jpg");
        item.setDescription("desc");
        item.setIsbn("1234567890");
        item.setPubDate("2023-10-10");
        item.setPublisher("Tech Publisher");
        item.setStandardPrice(30000);
        item.setSalesPrice(27000);

        BookSearchApiResponse apiResponse = new BookSearchApiResponse();
        apiResponse.setItems(List.of(item));

        when(restTemplate.getForObject(anyString(), eq(BookSearchApiResponse.class))).thenReturn(apiResponse);

        List<BookSearchResponse> responses = bookService.searchBook("Java");
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).title()).isEqualTo("Java Programming");
    }

    @Test
    @DisplayName("알라딘 검색 API 응답 null")
    void testSearchBookNullResponse() {
        when(restTemplate.getForObject(anyString(), eq(BookSearchApiResponse.class))).thenReturn(null);

        List<BookSearchResponse> responses = bookService.searchBook("test");
        assertThat(responses).isEmpty();
    }

    @Test
    @DisplayName("알라딘 검색 API RestClientException 발생")
    void testSearchBookError() {
        when(restTemplate.getForObject(anyString(), eq(BookSearchApiResponse.class))).thenThrow(new RestClientException("error"));
        assertThrows(BookSearchException.class, () -> bookService.searchBook("test"));
    }

    @Test
    @DisplayName("이미 존재하는 ISBN으로 책 등록시 BookAlreadyExistsException")
    void testRegisterBookAlreadyExists() {
        when(bookRepository.findByIsbn("isbn")).thenReturn(Optional.of(new Book()));
        BookCreateRequest request = new BookCreateRequest("title", "index", "desc", "2023-10-10", "isbn", "img", true, 10, 20000, BigDecimal.TEN, "publisher", "카테고리", "홍길동(지은이)");
        assertThrows(BookAlreadyExistsException.class, () -> bookService.registerBook(request));
    }

    @Test
    @DisplayName("책 등록 성공 테스트")
    void testRegisterBookSuccess() {
        BookCreateRequest request = new BookCreateRequest("title", "index", "desc", "2023-10-10", "isbn", "img", true, 10, 20000, BigDecimal.TEN, "publisher", "상위 > 하위", "홍길동(지은이),김영희(옮긴이)");

        when(bookRepository.findByIsbn("isbn")).thenReturn(Optional.empty());
        when(bookRepository.save(any())).thenAnswer(invocation -> {
            Book b = invocation.getArgument(0);
            b.setId(1L);
            b.setPublishedAt(Date.valueOf(LocalDate.now()));
            b.setPrice(BigDecimal.TEN);
            return b;
        });

        when(publisherRepository.findByName("publisher")).thenReturn(Optional.empty());
        when(publisherRepository.save(any())).thenAnswer(invocation -> {
            Publisher p = invocation.getArgument(0);
            p.setId(1L);
            p.setName("publisher");
            return p;
        });

        when(categoryRepository.findByNameAndParentCategory(eq("상위"), isNull())).thenReturn(Optional.empty());
        when(categoryRepository.findByNameAndParentCategory(eq("하위"), any())).thenReturn(Optional.empty());

        when(contributorRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(contributorRepository.save(any())).thenAnswer(invocation -> {
            Contributor ctr = invocation.getArgument(0);
            if (ctr.getName() == null) {
                ctr.setName("기타");
            }
            ctr.setId(11L);
            return ctr;
        });

        when(roleRepository.findByRoleName(any())).thenReturn(Optional.empty());
        when(roleRepository.save(any())).thenAnswer(invocation -> {
            Role r = invocation.getArgument(0);
            r.setId(1L);
            return r;
        });

        BookCreateResponse response = bookService.registerBook(request);
        assertThat(response.title()).isEqualTo("title");
        verify(bookRepository, times(2)).save(any(Book.class));
    }

    @Test
    @DisplayName("책 업데이트시 BookNotFoundException 발생")
    void testUpdateBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        BookUpdateRequest request = new BookUpdateRequest(
                "title",
                "index",
                "desc",
                "2023-10-10",
                "1111122222333",
                "example.jpg",
                List.of("detail.jpg"),
                true,
                10,
                20000,
                BigDecimal.TEN,
                "IN_STOCK",
                "publisher",
                List.of("카테고리"),
                "홍길동(지은이)",
                List.of("tag1", "tag2")
        );

        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(1L, request));
    }

    @Test
    @DisplayName("책 업데이트 성공")
    void testUpdateBookSuccess() {
        Publisher oldPublisher = new Publisher();
        oldPublisher.setName("oldPublisher");
        oldPublisher.setId(2L);

        Book book = new Book();
        book.setId(1L);
        book.setPublisher(oldPublisher);
        book.setBookImages(new HashSet<>());
        book.setTags(new HashSet<>());
        book.setCategories(new ArrayList<>());
        book.setContributors(new HashSet<>());
        book.setPublishedAt(Date.valueOf(LocalDate.now()));
        book.setPrice(BigDecimal.valueOf(10000));

        when(bookRepository.findById(eq(1L))).thenReturn(Optional.of(book));
        when(publisherRepository.findByName(eq("publisher"))).thenReturn(Optional.empty());
        when(publisherRepository.save(any())).thenAnswer(invocation -> {
            Publisher p = invocation.getArgument(0);
            p.setId(10L);
            p.setName("publisher");
            return p;
        });
        when(contributorRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(contributorRepository.save(any())).thenAnswer(invocation -> {
            Contributor c = invocation.getArgument(0);
            c.setId(3L);
            c.setName("홍길동");
            return c;
        });

        when(roleRepository.findByRoleName(any())).thenReturn(Optional.empty());
        when(roleRepository.save(any())).thenAnswer(invocation -> {
            Role r = invocation.getArgument(0);
            r.setId(2L);
            return r;
        });

        when(tagRepository.save(any())).thenAnswer(invocation -> {
            Tag t = invocation.getArgument(0);
            t.setId(100L); // 임의의 ID 할당
            return t;
        });

        when(bookRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        when(categoryRepository.findByNameAndParentCategory(eq("카테고리"), isNull())).thenReturn(Optional.empty());

        BookUpdateRequest request = new BookUpdateRequest(
                "title",
                "index",
                "desc",
                "2023-10-10",
                "1111122222333",
                "example.jpg",
                List.of("detail.jpg"),
                true,
                10,
                20000,
                BigDecimal.TEN,
                "IN_STOCK",
                "publisher",
                List.of("카테고리"),
                "홍길동(지은이)",
                List.of("tag1", "tag2")
        );

        BookUpdateResponse response = bookService.updateBook(1L, request);
        assertThat(response.title()).isEqualTo("title");
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("책 삭제시 BookNotFoundException")
    void testDeleteBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(1L));
    }

    @Test
    @DisplayName("책 삭제 성공")
    void testDeleteBookSuccess() {
        Publisher publisher = new Publisher();
        publisher.setName("publisher");
        publisher.setId(11L);
        Book book = new Book();
        book.setId(1L);
        book.setStatus(BookStatus.IN_STOCK);
        book.setPublisher(publisher);
        book.setPublishedAt(Date.valueOf(LocalDate.now()));
        book.setPrice(BigDecimal.TEN);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        BookDeleteResponse response = bookService.deleteBook(1L);
        assertThat(response.message()).isEqualTo("Book deleted successfully.");
        assertThat(book.getStatus()).isEqualTo(BookStatus.DELETED);
    }

    @Test
    @DisplayName("getBook - BookNotFoundException")
    void testGetBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.getBook(1L));
    }

    @Test
    @DisplayName("getBook - DELETED 상태일때 BookNotFoundException")
    void testGetBookDeleted() {
        Book book = new Book();
        book.setId(1L);
        book.setStatus(BookStatus.DELETED);
        book.setPublishedAt(Date.valueOf(LocalDate.now()));
        book.setPrice(BigDecimal.TEN);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        assertThrows(BookNotFoundException.class, () -> bookService.getBook(1L));
    }

    @Test
    @DisplayName("getBook 성공")
    void testGetBookSuccess() {
        Publisher publisher = new Publisher();
        publisher.setName("publisher");
        publisher.setId(20L);
        Book book = new Book();
        book.setId(1L);
        book.setStatus(BookStatus.IN_STOCK);
        book.setPublisher(publisher);
        book.setCategories(new ArrayList<>());
        book.setContributors(new HashSet<>());
        book.setTags(new HashSet<>());
        book.setPublishedAt(Date.valueOf(LocalDate.now()));
        book.setPrice(BigDecimal.valueOf(15000));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        BookDetailResponse response = bookService.getBook(1L);
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("parseAuthors 테스트 - 역할 미포함")
    void testParseAuthorsNoRole() throws Exception {
        BookCreateRequest request = new BookCreateRequest("title", "index", "desc", "2023-10-10", "isbn", "img", true, 10, 20000, BigDecimal.TEN, "publisher", "상위 > 하위", "홍길동,김영희");
        when(bookRepository.findByIsbn(eq("isbn"))).thenReturn(Optional.empty());

        when(publisherRepository.findByName(eq("publisher"))).thenReturn(Optional.empty());
        when(publisherRepository.save(any())).thenAnswer(invocation -> {
            Publisher p = invocation.getArgument(0);
            p.setId(1L);
            p.setName("publisher");
            return p;
        });

        when(categoryRepository.findByNameAndParentCategory(eq("상위"), isNull())).thenReturn(Optional.empty());
        when(categoryRepository.findByNameAndParentCategory(eq("하위"), any())).thenReturn(Optional.empty());

        when(contributorRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(contributorRepository.save(any())).thenAnswer(invocation -> {
            Contributor ctr = invocation.getArgument(0);
            ctr.setId(11L);
            if (ctr.getName() == null) {
                ctr.setName("기타");
            }
            return ctr;
        });

        when(roleRepository.findByRoleName(any())).thenReturn(Optional.empty());
        when(roleRepository.save(any())).thenAnswer(invocation -> {
            Role r = invocation.getArgument(0);
            r.setId(10L);
            return r;
        });

        when(bookRepository.save(any())).thenAnswer(invocation -> {
            Book b = invocation.getArgument(0);
            b.setId(1L);
            b.setPublishedAt(Date.valueOf(LocalDate.now()));
            b.setPrice(BigDecimal.TEN);
            return b;
        });

        BookCreateResponse response = bookService.registerBook(request);
        assertThat(response.authors()).contains("홍길동", "김영희");
    }

    @Test
    @DisplayName("mapToBookSearchResponse 날짜 파싱 예외 처리 커버")
    void testMapToBookSearchResponseDateParsingError() {
        BookSearchApiResponse.Item item = new BookSearchApiResponse.Item();
        item.setTitle("book");
        item.setPubDate("invalid-date");

        BookSearchApiResponse apiResponse = new BookSearchApiResponse();
        apiResponse.setItems(List.of(item));

        when(restTemplate.getForObject(anyString(), eq(BookSearchApiResponse.class))).thenReturn(apiResponse);

        List<BookSearchResponse> result = bookService.searchBook("test");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).pubDate()).isNotNull();
    }

    @Test
    @DisplayName("processCategoryHierarchy no input")
    void testProcessCategoryHierarchyNoInput() {
        BookCreateRequest request = new BookCreateRequest("title", "index", "desc", "2023-10-10", "isbn", "img", true, 10, 20000, BigDecimal.TEN, "publisher", "", "홍길동(지은이)");
        when(bookRepository.findByIsbn(eq("isbn"))).thenReturn(Optional.empty());
        when(publisherRepository.findByName(eq("publisher"))).thenReturn(Optional.empty());
        when(publisherRepository.save(any())).thenAnswer(invocation -> {
            Publisher p = invocation.getArgument(0);
            p.setId(1L);
            p.setName("publisher");
            return p;
        });

        when(contributorRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(contributorRepository.save(any())).thenAnswer(invocation -> {
            Contributor ctr = invocation.getArgument(0);
            ctr.setId(11L);
            ctr.setName("홍길동");
            return ctr;
        });

        when(roleRepository.findByRoleName(any())).thenReturn(Optional.empty());
        when(roleRepository.save(any())).thenAnswer(invocation -> {
            Role r = invocation.getArgument(0);
            r.setId(10L);
            return r;
        });

        when(bookRepository.save(any())).thenAnswer(invocation -> {
            Book b = invocation.getArgument(0);
            b.setId(1L);
            b.setPublishedAt(Date.valueOf(LocalDate.now()));
            b.setPrice(BigDecimal.TEN);
            return b;
        });

        BookCreateResponse response = bookService.registerBook(request);
        assertThat(response.categories()).isEmpty();
        verify(categoryRepository, never()).findByNameAndParentCategory(anyString(), any());
    }

    @Test
    @DisplayName("인기도 증가")
    void increasePopularity() {
        Long bookId = 1L;
        bookService.increasePopularity(bookId);
        verify(bookRepository, times(1)).increasePopularityByBookId(bookId);
    }

    @Test
    void getBooksSummary() {
        List<Long> bookIds = Arrays.asList(1L, 2L);

        Book book1 = new Book(1L, "title", "index", "discription", Date.valueOf(LocalDate.now()), "isbn", false, 10, BigDecimal.valueOf(15000), BigDecimal.valueOf(10.0), 0, BookStatus.IN_STOCK);
        Book book2 = new Book(2L, "title", "index", "discription", Date.valueOf(LocalDate.now()), "isbn", false, 10, BigDecimal.valueOf(15000), BigDecimal.valueOf(10.0), 0, BookStatus.IN_STOCK);

        BookSearchCondition condition = new BookSearchCondition(bookIds, ImageType.THUMBNAIL);
        when(bookRepository.findAllByBookSearchCondition(eq(condition))).thenReturn(Arrays.asList(book1, book2));

        List<BookSummaryResponse> responses = bookService.getBooksSummary(bookIds);
        assertThat(responses).hasSize(2);
        verify(bookRepository, times(1)).findAllByBookSearchCondition(condition);
    }

    @Test
    void getBooks() {
        Publisher publisher = new Publisher(1L, "publisher");
        Book book1 = new Book(1L, "title", "index", "discription", Date.valueOf(LocalDate.now()), "isbn", false, 10, BigDecimal.valueOf(15000), BigDecimal.valueOf(10.0), 0, BookStatus.IN_STOCK, publisher);
        Book book2 = new Book(2L, "title", "index", "discription", Date.valueOf(LocalDate.now()), "isbn", false, 10, BigDecimal.valueOf(15000), BigDecimal.valueOf(10.0), 0, BookStatus.IN_STOCK, publisher);

        PageRequest pageRequest = PageRequest.of(0, 2);
        when(bookRepository.findAllByPageable(eq(pageRequest))).thenReturn(new PageImpl<>(Arrays.asList(book1, book2)));

        Page<BookResponse> result = bookService.getBooks(pageRequest);
        assertThat(result.getContent()).hasSize(2);
        verify(bookRepository, times(1)).findAllByPageable(pageRequest);
    }

    @Test
    void getBooksByCategoryId() {
        Publisher publisher = new Publisher(1L, "publisher");
        PageRequest pageRequest = PageRequest.of(0, 2);

        Book book1 = new Book(1L, "title", "index", "discription", Date.valueOf(LocalDate.now()), "isbn", false, 10, BigDecimal.valueOf(15000), BigDecimal.valueOf(10.0), 0, BookStatus.IN_STOCK, publisher);
        Book book2 = new Book(2L, "title", "index", "discription", Date.valueOf(LocalDate.now()), "isbn", false, 10, BigDecimal.valueOf(15000), BigDecimal.valueOf(10.0), 0, BookStatus.IN_STOCK, publisher);

        List<Long> categoryIds = List.of(1L, 2L);
        when(categoryRepository.findSubCategoryIdsByCategoryId(eq(1L))).thenReturn(categoryIds);
        when(bookRepository.findAllByCategoryIds(eq(categoryIds), eq(pageRequest))).thenReturn(new PageImpl<>(List.of(book1, book2)));

        Page<BookResponse> result = bookService.getBooksByCategoryId(1L, pageRequest);
        assertThat(result.getContent()).hasSize(2);
        verify(categoryRepository, times(1)).findSubCategoryIdsByCategoryId(1L);
    }

    @Test
    void getBookDetail() {
        Publisher publisher = new Publisher(1L, "publisher");
        Book book = new Book(1L, "title", "index", "discription", Date.valueOf(LocalDate.now()), "isbn", false, 10, BigDecimal.valueOf(15000), BigDecimal.valueOf(10.0), 0, BookStatus.IN_STOCK, publisher);

        when(bookRepository.findByBookId(eq(1L))).thenReturn(Optional.of(book));
        BookViewResponse bookDetail = bookService.getBookDetail(1L);

        assertNotNull(bookDetail);
        verify(bookRepository, times(1)).findByBookId(1L);
    }

    @Test
    void getBooksDetail() {
        Publisher publisher = new Publisher(1L, "publisher");
        Book book = new Book(1L, "title", "index", "discription", Date.valueOf(LocalDate.now()), "isbn", false, 10, BigDecimal.valueOf(15000), BigDecimal.valueOf(10.0), 0, BookStatus.IN_STOCK, publisher);

        PageRequest pageRequest = PageRequest.of(0, 2);
        when(bookRepository.findAllByStatusNot(eq(BookStatus.DELETED), eq(pageRequest))).thenReturn(new PageImpl<>(Arrays.asList(book)));

        Page<BookDetailResponse> booksDetail = bookService.getBooksDetail(pageRequest);
        assertThat(booksDetail.getContent()).hasSize(1);
        verify(bookRepository, times(1)).findAllByStatusNot(BookStatus.DELETED, pageRequest);
    }

}