package com.nhnacademy.heukbaekbookshop.category.repository.impl;

import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.category.domain.QCategory;
import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.nhnacademy.heukbaekbookshop.category.domain.QCategory.*;

public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CategoryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Long> findAllCategoryIdsByCategoryId(Long categoryId) {
        return queryFactory
                .select(category.id)
                .from(category)
                .where(category.id.eq(categoryId)
                        .or(category.parentCategory.id.eq(categoryId))) // 상위 카테고리 포함
                .fetch();
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
