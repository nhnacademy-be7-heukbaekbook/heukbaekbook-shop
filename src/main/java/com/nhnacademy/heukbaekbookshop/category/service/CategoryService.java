package com.nhnacademy.heukbaekbookshop.category.service;

import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.category.dto.request.CategoryCreateRequest;
import com.nhnacademy.heukbaekbookshop.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.heukbaekbookshop.category.dto.response.*;
import com.nhnacademy.heukbaekbookshop.category.exception.CategoryAlreadyExistsException;
import com.nhnacademy.heukbaekbookshop.category.exception.CategoryNotFoundException;
import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryCreateResponse registerCategory(CategoryCreateRequest request) {
        Category parentCategory = null;
        if (request.parentId() != null) {
            parentCategory = categoryRepository.findById(request.parentId())
                    .orElseThrow(() -> new CategoryNotFoundException("부모 카테고리가 존재하지 않습니다."));
        }

        if (categoryRepository.findByParentCategory_IdAndName(request.parentId(), request.name()).isPresent()) {
            throw new CategoryAlreadyExistsException("이미 존재하는 카테고리입니다.");
        }

        Category category = new Category(parentCategory, request.name());
        categoryRepository.save(category);

        Long parentId = (parentCategory != null) ? parentCategory.getId() : null;
        return new CategoryCreateResponse(parentId, category.getName());
    }

    @Transactional
    public CategoryUpdateResponse updateCategory(Long id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("존재하지 않는 카테고리입니다."));

        Category parentCategory = null;
        if (request.parentId() != null) {
            parentCategory = categoryRepository.findById(request.parentId())
                    .orElseThrow(() -> new CategoryNotFoundException("부모 카테고리가 존재하지 않습니다."));
        }

        category.setParentCategory(parentCategory);
        category.setName(request.name());

        categoryRepository.save(category);

        Long parentId = parentCategory.getId();
        return new CategoryUpdateResponse(parentId, category.getName());
    }

    @Transactional
    public CategoryDeleteResponse deleteCategory(Long id) {
        if (categoryRepository.findById(id).isEmpty()) {
            throw new CategoryNotFoundException("존재하지 않는 카테고리 입니다.");
        }
        categoryRepository.deleteById(id);
        return new CategoryDeleteResponse("카테고리가 정상적으로 삭제되었습니다.");
    }

    public CategoryDetailResponse getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("존재하지 않는 카테고리입니다."));

        Long parentId = (category.getParentCategory() != null) ? category.getParentCategory().getId() : null;
        return new CategoryDetailResponse(category.getId(), parentId, category.getName());
    }

    public Page<CategoryDetailResponse> getCategories(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAllBy(pageable);
        return categories.map(category -> new CategoryDetailResponse(
                category.getId(),
                (category.getParentCategory() != null) ? category.getParentCategory().getId() : null,
                category.getName()

        ));
    }

    public List<String> getCategoryPaths() {
        List<Category> categories = categoryRepository.findAll();

        Map<Long, Category> categoryMap = categories.stream()
                .collect(Collectors.toMap(Category::getId, category -> category));

        List<String> categoryPaths = new ArrayList<>();
        for (Category category : categories) {
            categoryPaths.add(buildCategoryPath(category, categoryMap));
        }

        return categoryPaths.stream().distinct().collect(Collectors.toList());
    }

    private String buildCategoryPath(Category category, Map<Long, Category> categoryMap) {
        if (category.getParentCategory() == null) {
            return category.getName();
        }

        Category parent = categoryMap.get(category.getParentCategory().getId());
        if (parent == null) {
            return category.getName();
        }

        return buildCategoryPath(parent, categoryMap) + ">" + category.getName();
    }

    public List<CategorySummaryResponse> getTopCategories() {
        List<Category> parentCategories = categoryRepository.findTopCategories();

        return parentCategories.stream()
                .map(this::toCategorySummaryResponse)
                .collect(Collectors.toList());
    }

    private CategorySummaryResponse toCategorySummaryResponse(Category category) {
        List<CategorySummaryResponse> subCategories = category.getSubCategories().stream()
                .map(this::toCategorySummaryResponse)
                .collect(Collectors.toList());
        return new CategorySummaryResponse(category.getId(), category.getName(), subCategories);
    }
}
