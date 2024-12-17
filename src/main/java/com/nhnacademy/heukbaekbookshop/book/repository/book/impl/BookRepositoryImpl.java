package com.nhnacademy.heukbaekbookshop.book.repository.book.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.domain.BookStatus;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchCondition;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepositoryCustom;
import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.nhnacademy.heukbaekbookshop.book.domain.QBook.book;
import static com.nhnacademy.heukbaekbookshop.book.domain.QBookCategory.bookCategory;
import static com.nhnacademy.heukbaekbookshop.contributor.domain.QPublisher.publisher;
import static com.nhnacademy.heukbaekbookshop.image.domain.QBookImage.bookImage;

public class BookRepositoryImpl implements BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BookRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Book> findAllByBookSearchCondition(BookSearchCondition condition) {
        return queryFactory
                .selectFrom(book)
                .join(book.bookImages, bookImage).fetchJoin()
                .where(
                        book.status.eq(BookStatus.IN_STOCK),
                        bookIdIn(condition.bookIds()),
                        bookImageTypeEq(condition.imageType())
                )
                .orderBy(bookImage.id.asc())
                .fetch();
    }

    private BooleanExpression bookIdIn(List<Long> bookIds) {
        return bookIds == null ? null : book.id.in(bookIds);
    }

    private BooleanExpression bookImageTypeEq(ImageType type) {
        return type == null ? null : bookImage.type.eq(type);
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
                .where(book.status.eq(BookStatus.IN_STOCK))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Book> findAllByCategoryIds(List<Long> categoryIds, Pageable pageable) {
        List<Long> bookIds = queryFactory
                .select(book.id)
                .from(bookCategory)
                .join(bookCategory.book, book)
                .where(
                        bookCategory.categoryId.in(categoryIds),
                        book.status.eq(BookStatus.IN_STOCK)
                )
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Book> books = queryFactory
                .selectFrom(book)
                .join(book.publisher, publisher).fetchJoin()
                .where(book.id.in(bookIds))
                .fetch();

        Long total = queryFactory
                .select(bookCategory.book.id.countDistinct())
                .from(bookCategory)
                .where(
                        bookCategory.categoryId.in(categoryIds),
                        book.status.eq(BookStatus.IN_STOCK)
                )
                .fetchOne();

        return new PageImpl<>(books, pageable, total);
    }

    @Override
    public Optional<Book> findByBookId(Long bookId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(book)
                .join(book.publisher, publisher).fetchJoin()
                .where(book.id.eq(bookId))
                .fetchOne());
    }

    @Override
    public void increasePopularityByBookId(Long bookId) {
        queryFactory
                .update(book)
                .set(book.popularity, book.popularity.add(1))
                .where(book.id.eq(bookId))
                .execute();
    }
}
