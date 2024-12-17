package com.nhnacademy.heukbaekbookshop.book.repository.book;

import com.nhnacademy.heukbaekbookshop.book.domain.BookCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql("book-test.sql")
class BookCategoryRepositoryCustomTest {

    @Autowired
    private BookCategoryRepository bookCategoryRepository;

    @Test
    void findBookCategoriesByBookId() {
        //given
        Long bookId = 1L;

        //when
        Optional<BookCategory> result = bookCategoryRepository.findBookCategoriesByBookId(bookId);

        //then
        assertTrue(result.isPresent());
        assertEquals(bookId, result.get().getBookId());
    }

    @Test
    void findBookCategoriesByBookId_fail() {
        //given
        Long bookId = 5L;

        //when
        Optional<BookCategory> result = bookCategoryRepository.findBookCategoriesByBookId(bookId);

        //then
        assertFalse(result.isPresent());
        assertEquals(Optional.empty(), result);
    }
}