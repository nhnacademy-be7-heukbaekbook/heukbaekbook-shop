package com.nhnacademy.heukbaekbookshop.category.repository.impl;

import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.nhnacademy.heukbaekbookshop.category.domain.QCategory.category;

public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CategoryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Category> findTopCategories() {
        return queryFactory
                .selectFrom(category)
                .where(
                        category.parentCategory.isNull()
                )
                .orderBy(category.name.asc())
                .fetch();
    }
}
