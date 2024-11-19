package com.nhnacademy.heukbaekbookshop.category.controller;

import com.nhnacademy.heukbaekbookshop.category.dto.response.CategorySummaryResponse;
import com.nhnacademy.heukbaekbookshop.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategorySummaryResponse> getParentCategories() {
        return categoryService.getTopCategories();
    }
}
