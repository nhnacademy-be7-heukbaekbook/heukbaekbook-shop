package com.nhnacademy.heukbaekbookshop.order.repository.impl;

import com.nhnacademy.heukbaekbookshop.order.domain.WrappingPaper;
import com.nhnacademy.heukbaekbookshop.order.repository.WrappingPaperRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.nhnacademy.heukbaekbookshop.image.domain.QWrappingPaperImage.wrappingPaperImage;
import static com.nhnacademy.heukbaekbookshop.order.domain.QWrappingPaper.wrappingPaper;

public class WrappingPaperRepositoryImpl implements WrappingPaperRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public WrappingPaperRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<WrappingPaper> searchById(Long id) {
        return Optional.ofNullable(queryFactory
                .selectFrom(wrappingPaper)
                .join(wrappingPaper.wrappingPaperImage, wrappingPaperImage).fetchJoin()
                .where(wrappingPaper.id.eq(id))
                .fetchOne());
    }

    @Override
    public List<WrappingPaper> searchAll() {
        return queryFactory
                .selectFrom(wrappingPaper)
                .join(wrappingPaper.wrappingPaperImage, wrappingPaperImage).fetchJoin()
                .fetch();

    }
}
