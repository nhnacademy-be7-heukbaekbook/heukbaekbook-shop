package com.nhnacademy.heukbaekbookshop.category.controller;

import com.nhnacademy.heukbaekbookshop.category.dto.request.CategoryCreateRequest;
import com.nhnacademy.heukbaekbookshop.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryCreateResponse;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryDeleteResponse;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryUpdateResponse;
import com.nhnacademy.heukbaekbookshop.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/admins")
    public ResponseEntity<CategoryCreateResponse> createCategory(@RequestBody CategoryCreateRequest request) {
        CategoryCreateResponse response = categoryService.registerCategory(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/admins/{id}")
    public ResponseEntity<CategoryUpdateResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryUpdateRequest request) {
        CategoryUpdateResponse response = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admins/{id}")
    public ResponseEntity<CategoryDeleteResponse> deleteCategory(@PathVariable Long id) {
        CategoryDeleteResponse response = categoryService.deleteCategory(id);
        return ResponseEntity.ok(response);
    }

}
