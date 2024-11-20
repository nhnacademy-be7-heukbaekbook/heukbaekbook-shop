//package com.nhnacademy.heukbaekbookshop.category.repository.impl;
//
//import com.nhnacademy.heukbaekbookshop.category.domain.Category;
//import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@DataJpaTest
//@Transactional
//class CategoryRepositoryImplTest {
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @BeforeEach
//    void setUp() {
//        Category category1 = new Category(null, "상위1");
//        Category category2 = new Category(null, "상위2");
//        Category category3 = new Category(null, "상위3");
//
//        Category category4 = new Category(category1, "하위1");
//        Category category5 = new Category(category1, "하위2");
//        Category category6 = new Category(category2, "하위3");
//        Category category7 = new Category(category2, "하위4");
//
//        category1.getSubCategories().add(category4);
//        category1.getSubCategories().add(category5);
//        category2.getSubCategories().add(category6);
//        category2.getSubCategories().add(category7);
//
//        categoryRepository.save(category1);
//        categoryRepository.save(category2);
//        categoryRepository.save(category3);
//        categoryRepository.save(category4);
//        categoryRepository.save(category5);
//        categoryRepository.save(category6);
//        categoryRepository.save(category7);
//    }
//
//
//    @Test
//    void findAll() {
//        List<Category> categoryList = categoryRepository.findTopCategories();
//
//        Assertions.assertThat(3).isEqualTo(categoryList.size());
//    }
//}