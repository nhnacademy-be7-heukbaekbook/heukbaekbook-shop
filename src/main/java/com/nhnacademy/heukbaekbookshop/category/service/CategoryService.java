package com.nhnacademy.heukbaekbookshop.category.service;

import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.category.dto.request.CategoryCreateRequest;
import com.nhnacademy.heukbaekbookshop.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryCreateResponse;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryDeleteResponse;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryUpdateResponse;
import com.nhnacademy.heukbaekbookshop.category.exception.CategoryAlreadyExistsException;
import com.nhnacademy.heukbaekbookshop.category.exception.CategoryNotFoundException;
import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

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

        Long parentId = (parentCategory != null) ? parentCategory.getId() : null;
        return new CategoryUpdateResponse(parentId, category.getName());
    }

    public CategoryDeleteResponse deleteCategory(Long id) {
        if (categoryRepository.findById(id).isEmpty())  {
            throw new CategoryNotFoundException("존재하지 않는 카테고리 입니다.");
        }
        categoryRepository.deleteById(id);
        return new CategoryDeleteResponse("카테고리가 정상적으로 삭제되었습니다.");
    }
}
