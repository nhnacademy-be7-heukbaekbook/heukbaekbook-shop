package com.nhnacademy.heukbaekbookshop.book.service.book;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.domain.BookStatus;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchCondition;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookDetailResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookSummaryResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookViewResponse;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepository;
import com.nhnacademy.heukbaekbookshop.contributor.domain.Publisher;
import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void searchBook() {
    }

    @Test
    void registerBook() {
    }

    @Test
    void updateBook() {
    }

    @Test
    void deleteBook() {
    }

    @Test
    void getBook() {
    }

    @Test
    @DisplayName("인기도 증가")
    void increasePopularity() {
        //given
        Long bookId = 1L;

        //when
        bookService.increasePopularity(bookId);

        //then
        verify(bookRepository, times(1)).increasePopularityByBookId(bookId);
    }

    @Test
    void getBooksSummary() {
        //given
        List<Long> bookIds = Arrays.asList(1L, 2L);

        Book book1 = new Book(1L, "title", "index", "discription", Date.valueOf(LocalDate.now()), "isbn", false, 10, BigDecimal.valueOf(15000), BigDecimal.valueOf(10.0), 0, BookStatus.IN_STOCK);
        Book book2 = new Book(2L, "title", "index", "discription", Date.valueOf(LocalDate.now()), "isbn", false, 10, BigDecimal.valueOf(15000), BigDecimal.valueOf(10.0), 0, BookStatus.IN_STOCK);

        BookSearchCondition condition = new BookSearchCondition(bookIds, ImageType.THUMBNAIL);

        when(bookRepository.findAllByBookSearchCondition(condition)).thenReturn(Arrays.asList(book1, book2));

        // when
        List<BookSummaryResponse> responses = bookService.getBooksSummary(bookIds);

        // then
        assertThat(responses).hasSize(2);
        verify(bookRepository, times(1)).findAllByBookSearchCondition(condition);
    }

    @Test
    void getBooks() {
        //given
        Publisher publisher = new Publisher(1L, "publisher");

        Book book1 = new Book(1L, "title", "index", "discription", Date.valueOf(LocalDate.now()), "isbn", false, 10, BigDecimal.valueOf(15000), BigDecimal.valueOf(10.0), 0, BookStatus.IN_STOCK, publisher);
        Book book2 = new Book(2L, "title", "index", "discription", Date.valueOf(LocalDate.now()), "isbn", false, 10, BigDecimal.valueOf(15000), BigDecimal.valueOf(10.0), 0, BookStatus.IN_STOCK, publisher);

        PageRequest pageRequest = PageRequest.of(0, 2);

        when(bookRepository.findAllByPageable(pageRequest)).thenReturn(new PageImpl<>(Arrays.asList(book1, book2)));

        //when
        Page<BookResponse> result = bookService.getBooks(pageRequest);

        //then
        assertThat(result.getContent()).hasSize(2);
        verify(bookRepository, times(1)).findAllByPageable(pageRequest);
    }

    public Page<BookResponse> getBooksByCategoryId(Long categoryId, Pageable pageable) {
        List<Long> categoryIds = categoryRepository.findSubCategoryIdsByCategoryId(categoryId);
        Page<Book> books = bookRepository.findAllByCategoryIds(categoryIds, pageable);

        return books.map(BookResponse::of);
    }

    @Test
    void getBooksByCategoryId() {
        //given
        Publisher publisher = new Publisher(1L, "publisher");

        Category category = Category.createRootCategory("상위카테고리1");
        Category subCategory = Category.createSubCategory("하위카테고리1", category);

        List<Long> categoryIds = List.of(1L, 2L);

        PageRequest pageRequest = PageRequest.of(0, 2);

        Book book1 = new Book(1L, "title", "index", "discription", Date.valueOf(LocalDate.now()), "isbn", false, 10, BigDecimal.valueOf(15000), BigDecimal.valueOf(10.0), 0, BookStatus.IN_STOCK, publisher);
        Book book2 = new Book(2L, "title", "index", "discription", Date.valueOf(LocalDate.now()), "isbn", false, 10, BigDecimal.valueOf(15000), BigDecimal.valueOf(10.0), 0, BookStatus.IN_STOCK, publisher);

        List<Book> books = List.of(book1, book2);

        when(categoryRepository.findSubCategoryIdsByCategoryId(1L)).thenReturn(categoryIds);
        when(bookRepository.findAllByCategoryIds(categoryIds, pageRequest)).thenReturn(new PageImpl<>(books));

        //when
        Page<BookResponse> result = bookService.getBooksByCategoryId(1L, pageRequest);

        //then
        assertThat(result.getContent()).hasSize(2);
        verify(categoryRepository, times(1)).findSubCategoryIdsByCategoryId(1L);
    }

    @Test
    void getBookDetail() {
        //given
        Publisher publisher = new Publisher(1L, "publisher");

        Category category = Category.createRootCategory("상위카테고리1");
        Category subCategory = Category.createSubCategory("하위카테고리1", category);

        Book book = new Book(1L, "title", "index", "discription", Date.valueOf(LocalDate.now()), "isbn", false, 10, BigDecimal.valueOf(15000), BigDecimal.valueOf(10.0), 0, BookStatus.IN_STOCK, publisher);

        when(bookRepository.findByBookId(1L)).thenReturn(Optional.of(book));

        //when
        BookViewResponse bookDetail = bookService.getBookDetail(1L);

        //then
        assertNotNull(bookDetail);
        verify(bookRepository, times(1)).findByBookId(1L);
    }

    @Test
    void getBooksDetail() {
        //given
        Publisher publisher = new Publisher(1L, "publisher");

        Category category = Category.createRootCategory("상위카테고리1");
        Category subCategory = Category.createSubCategory("하위카테고리1", category);

        Book book = new Book(1L, "title", "index", "discription", Date.valueOf(LocalDate.now()), "isbn", false, 10, BigDecimal.valueOf(15000), BigDecimal.valueOf(10.0), 0, BookStatus.IN_STOCK, publisher);

        PageRequest pageRequest = PageRequest.of(0, 2);

        when(bookRepository.findAllByStatusNot(BookStatus.DELETED, pageRequest)).thenReturn(new PageImpl<>(Arrays.asList(book)));

        //when
        Page<BookDetailResponse> booksDetail = bookService.getBooksDetail(pageRequest);

        //then
        assertThat(booksDetail.getContent()).hasSize(1);
        verify(bookRepository, times(1)).findAllByStatusNot(BookStatus.DELETED, pageRequest);

    }
}