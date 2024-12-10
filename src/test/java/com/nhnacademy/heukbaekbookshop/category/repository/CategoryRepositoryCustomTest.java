package com.nhnacademy.heukbaekbookshop.category.repository;

import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategorySummaryResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryCustomTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findTopCategories() {
        //given
        Category rootCategory1 = Category.createRootCategory("최상위카테고리1");
        Category rootCategory2 = Category.createRootCategory("최상위카테고리2");
        categoryRepository.save(rootCategory1);
        categoryRepository.save(rootCategory2);

        Category category1 = Category.createSubCategory("상위카테고리1", rootCategory1);
        Category category2 = Category.createSubCategory("상위카테고리2", rootCategory2);
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        Category subCategory1 = Category.createSubCategory("하위카테고리1", category1);
        Category subCategory2 = Category.createSubCategory("하위카테고리2", category1);
        categoryRepository.save(subCategory1);
        categoryRepository.save(subCategory2);

        //when
        List<Category> categories = categoryRepository.findTopCategories();

        //then
        List<CategorySummaryResponse> categorySummaryResponses = categories.stream()
                .map(this::toCategorySummaryResponse)
                .toList();

        assertThat(2).isEqualTo(categories.size());
        assertThat("최상위카테고리1").isEqualTo(categories.getFirst().getName());

    }

    private CategorySummaryResponse toCategorySummaryResponse(Category category) {
        List<CategorySummaryResponse> subCategories = category.getSubCategories().stream()
                .map(this::toCategorySummaryResponse)
                .collect(Collectors.toList());
        return new CategorySummaryResponse(category.getId(), category.getName(), subCategories);
    }
}