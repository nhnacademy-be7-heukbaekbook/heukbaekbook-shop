package com.nhnacademy.heukbaekbookshop.book.repository.book.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.QBook;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookSummaryResponse;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.nhnacademy.heukbaekbookshop.book.domain.QBook.*;

public class BookRepositoryImpl implements BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BookRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<BookSummaryResponse> findAllByIdIn(List<Long> bookIds) {
        return queryFactory
                .select(Projections.constructor(BookSummaryResponse.class,
                        book.id,
                        book.title,
                        book.price,
                        book.discountRate))
                .from(book)
                .where(book.id.in(bookIds))
                .fetch();
    }
}
