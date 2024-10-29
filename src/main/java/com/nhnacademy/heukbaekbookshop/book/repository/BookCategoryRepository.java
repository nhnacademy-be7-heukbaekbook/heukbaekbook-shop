package com.nhnacademy.heukbaekbookshop.book.repository;

import com.nhnacademy.heukbaekbookshop.book.domain.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {

}