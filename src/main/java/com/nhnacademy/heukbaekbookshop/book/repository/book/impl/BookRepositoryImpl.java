package com.nhnacademy.heukbaekbookshop.book.repository.book.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.domain.BookStatus;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepositoryCustom;
import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.nhnacademy.heukbaekbookshop.book.domain.QBook.book;
import static com.nhnacademy.heukbaekbookshop.contributor.domain.QPublisher.*;
import static com.nhnacademy.heukbaekbookshop.image.domain.QBookImage.bookImage;

public class BookRepositoryImpl implements BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BookRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Book> findAllByIdInAndType(List<Long> bookIds, ImageType type) {
        return queryFactory
                .selectFrom(book)
                .join(book.bookImages, bookImage).fetchJoin()
                .where(
                        book.status.eq(BookStatus.IN_STOCK),
                        book.id.in(bookIds),
                        bookImage.type.eq(type)
                )
                .orderBy(bookImage.id.asc())
                .fetch();
    }

    @Override
    public Page<Book> findAllByPageable(Pageable pageable) {
        List<Book> content = queryFactory
                .selectFrom(book)
                .join(book.publisher, publisher).fetchJoin()
                .where(book.status.eq(BookStatus.IN_STOCK))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(book.count())
                .from(book)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
