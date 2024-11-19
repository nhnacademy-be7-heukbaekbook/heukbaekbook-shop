package com.nhnacademy.heukbaekbookshop.book.repository.book.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.image.repository.BookImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
class BookRepositoryImplTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookImageRepository bookImageRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findAllByIdInAndType() {

    }

    @Test
    void findAllByPageable() {
    }
}