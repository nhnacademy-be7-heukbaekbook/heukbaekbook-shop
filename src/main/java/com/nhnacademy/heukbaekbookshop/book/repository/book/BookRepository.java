package com.nhnacademy.heukbaekbookshop.book.repository.book;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.domain.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom {
    Optional<Book> findByIsbn(String isbn);
    Page<Book> findAllByStatusNot(BookStatus status, Pageable pageable);
    List<Book> findAllByStatusNot(BookStatus status);
    List<Book> findAllByStatus(BookStatus status);
}