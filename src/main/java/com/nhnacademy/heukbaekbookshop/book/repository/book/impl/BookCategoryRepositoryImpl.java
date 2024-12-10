package com.nhnacademy.heukbaekbookshop.book.repository.book.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.BookCategory;
import com.nhnacademy.heukbaekbookshop.book.domain.BookStatus;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookCategoryRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.nhnacademy.heukbaekbookshop.book.domain.QBook.book;
import static com.nhnacademy.heukbaekbookshop.book.domain.QBookCategory.*;
import static com.nhnacademy.heukbaekbookshop.category.domain.QCategory.category;
import static com.nhnacademy.heukbaekbookshop.contributor.domain.QPublisher.*;

public class BookCategoryRepositoryImpl implements BookCategoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BookCategoryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<BookCategory> findBookCategoriesByBookId(Long bookId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(bookCategory)
                .join(bookCategory.category, category).fetchJoin()
                .where(bookCategory.bookId.eq(bookId))
                .fetchFirst());
    }
}
