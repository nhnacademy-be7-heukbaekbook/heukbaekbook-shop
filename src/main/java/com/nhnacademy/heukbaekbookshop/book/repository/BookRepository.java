package com.nhnacademy.heukbaekbookshop.book.repository;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, BookCustomRepository {
    Optional<Book> findByIsbn(String isbn);
}
