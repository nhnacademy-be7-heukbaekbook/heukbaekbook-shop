package com.nhnacademy.heukbaekbookshop.category.controller.admin;

import com.nhnacademy.heukbaekbookshop.category.dto.request.CategoryCreateRequest;
import com.nhnacademy.heukbaekbookshop.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryCreateResponse;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryDeleteResponse;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryDetailResponse;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryUpdateResponse;
import com.nhnacademy.heukbaekbookshop.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryCreateResponse> createCategory(@RequestBody CategoryCreateRequest request) {
        CategoryCreateResponse response = categoryService.registerCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryUpdateResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryUpdateRequest request) {
        CategoryUpdateResponse response = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDeleteResponse> deleteCategory(@PathVariable Long id) {
        CategoryDeleteResponse response = categoryService.deleteCategory(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDetailResponse> getCategory(@PathVariable Long id) {
        CategoryDetailResponse response = categoryService.getCategory(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryDetailResponse>> getCategories(Pageable pageable) {
        Page<CategoryDetailResponse> responses = categoryService.getCategories(pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> getCategoryPaths() {
        List<String> responses = categoryService.getCategoryPaths();
        return ResponseEntity.ok(responses);
    }

}