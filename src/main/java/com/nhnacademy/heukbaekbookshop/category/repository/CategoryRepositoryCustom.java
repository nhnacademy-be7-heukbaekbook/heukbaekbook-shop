package com.nhnacademy.heukbaekbookshop.category.repository;

import com.nhnacademy.heukbaekbookshop.category.domain.Category;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<Long> findAllCategoryIdsByCategoryId(Long categoryId);

    List<Category> findTopCategories();
}
