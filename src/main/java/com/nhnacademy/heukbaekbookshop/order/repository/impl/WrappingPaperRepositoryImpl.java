package com.nhnacademy.heukbaekbookshop.order.repository.impl;

import com.nhnacademy.heukbaekbookshop.image.domain.QWrappingPaperImage;
import com.nhnacademy.heukbaekbookshop.order.domain.QWrappingPaper;
import com.nhnacademy.heukbaekbookshop.order.domain.WrappingPaper;
import com.nhnacademy.heukbaekbookshop.order.repository.WrappingPaperRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.nhnacademy.heukbaekbookshop.image.domain.QWrappingPaperImage.*;
import static com.nhnacademy.heukbaekbookshop.order.domain.QWrappingPaper.*;

public class WrappingPaperRepositoryImpl implements WrappingPaperRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public WrappingPaperRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<WrappingPaper> searchAll() {
        return queryFactory
                .selectFrom(wrappingPaper)
                .join(wrappingPaper.wrappingPaperImage, wrappingPaperImage).fetchJoin()
                .fetch();

    }
}
