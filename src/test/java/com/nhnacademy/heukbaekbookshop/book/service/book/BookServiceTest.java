package com.nhnacademy.heukbaekbookshop.book.service.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    void getBooksSummary() {
    }

    @Test
    void getBooksList() {
    }
}