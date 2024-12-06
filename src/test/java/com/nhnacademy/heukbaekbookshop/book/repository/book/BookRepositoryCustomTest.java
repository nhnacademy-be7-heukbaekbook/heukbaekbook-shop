package com.nhnacademy.heukbaekbookshop.book.repository.book;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchCondition;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookNotFoundException;
import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql("book-test.sql")
class BookRepositoryCustomTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager em;

    @Test
    void findAllByBookSearchCondition() {
        //given
        List<Long> bookIds = List.of(1L, 2L, 3L);
        BookSearchCondition bookSearchCondition = new BookSearchCondition(bookIds, ImageType.THUMBNAIL);


        //when
        List<Book> books = bookRepository.findAllByBookSearchCondition(bookSearchCondition);

        //then
        assertEquals(2, books.size());

    }

    @Test
    void findAllByPageable() {
        //given
        PageRequest pageRequest = PageRequest.of(1, 1);
        
        //when
        Page<Book> result = bookRepository.findAllByPageable(pageRequest);

        //then
        assertEquals(3, result.getTotalPages());
        assertEquals(1, result.getNumber());
        assertEquals(1, result.getSize());
    }

    @Test
    void findAllByCategoryIds() {
        //given
        List<Long> categoryIds = List.of(1L, 2L, 3L);
        PageRequest pageRequest = PageRequest.of(1, 1);

        //when
        Page<Book> result = bookRepository.findAllByCategoryIds(categoryIds, pageRequest);

        //then
        assertEquals(3, result.getTotalPages());
        assertEquals(1, result.getNumber());
        assertEquals(1, result.getSize());

    }

    @Test
    void findByBookId() {
        //given
        Long bookId = 1L;

        //when
        Optional<Book> result = bookRepository.findByBookId(1L);

        //then
        assertTrue(result.isPresent());
        assertEquals(bookId, result.get().getId());
    }

    @Test
    void increasePopularityByBookId() {
        //given
        Long bookId = 1L;
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        long popularity = book.getPopularity();

        //when
        bookRepository.increasePopularityByBookId(bookId);

        em.flush();
        em.clear();

        //then
        Book updatedBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        assertEquals(popularity + 1, updatedBook.getPopularity());
    }
}