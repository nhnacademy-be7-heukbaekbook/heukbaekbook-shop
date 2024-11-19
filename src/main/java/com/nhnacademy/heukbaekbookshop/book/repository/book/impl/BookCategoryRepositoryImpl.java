package com.nhnacademy.heukbaekbookshop.book.repository.book.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.BookCategory;
import com.nhnacademy.heukbaekbookshop.book.domain.BookStatus;
import com.nhnacademy.heukbaekbookshop.book.domain.QBookCategory;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookCategoryRepositoryCustom;
import com.nhnacademy.heukbaekbookshop.contributor.domain.QPublisher;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.nhnacademy.heukbaekbookshop.book.domain.QBook.book;
import static com.nhnacademy.heukbaekbookshop.book.domain.QBookCategory.*;
import static com.nhnacademy.heukbaekbookshop.contributor.domain.QPublisher.*;

public class BookCategoryRepositoryImpl implements BookCategoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BookCategoryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BookCategory> findByCategoryIds(List<Long> categoryIds, Pageable pageable) {
        List<BookCategory> bookCategories = queryFactory
                .selectFrom(bookCategory)
                .join(bookCategory.book, book).fetchJoin()
                .join(book.publisher, publisher).fetchJoin()
                .where(
                        bookCategory.categoryId.in(categoryIds),
                        book.status.eq(BookStatus.IN_STOCK)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(bookCategory.count())
                .from(bookCategory)
                .where(bookCategory.categoryId.in(categoryIds))
                .fetchOne();

        return new PageImpl<>(bookCategories, pageable, total);
    }
}