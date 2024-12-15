package com.nhnacademy.heukbaekbookshop.book.repository.book.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.BookCategory;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookCategoryRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static com.nhnacademy.heukbaekbookshop.book.domain.QBookCategory.bookCategory;
import static com.nhnacademy.heukbaekbookshop.category.domain.QCategory.category;

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
