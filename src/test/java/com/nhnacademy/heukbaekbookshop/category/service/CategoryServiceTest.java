package com.nhnacademy.heukbaekbookshop.category.service;

import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.category.dto.request.CategoryCreateRequest;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryCreateResponse;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryDetailResponse;
import com.nhnacademy.heukbaekbookshop.category.exception.CategoryAlreadyExistsException;
import com.nhnacademy.heukbaekbookshop.category.exception.CategoryNotFoundException;
import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterCategory_SuccessWithoutParent() {
        // Given
        CategoryCreateRequest request = new CategoryCreateRequest(null, "Fiction");
        when(categoryRepository.findByParentCategory_IdAndName(null, "Fiction")).thenReturn(Optional.empty());

        // When
        CategoryCreateResponse response = categoryService.registerCategory(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Fiction");
        assertThat(response.parentId()).isNull();

        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(categoryRepository, times(1)).findByParentCategory_IdAndName(null, "Fiction");
    }

    @Test
    void testRegisterCategory_SuccessWithParent() {
        // Given
        Category parentCategory = new Category();
        parentCategory.setId(1L);
        parentCategory.setName("Parent Category");

        CategoryCreateRequest request = new CategoryCreateRequest(1L, "Child Category");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.findByParentCategory_IdAndName(1L, "Child Category")).thenReturn(Optional.empty());

        // When
        CategoryCreateResponse response = categoryService.registerCategory(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Child Category");
        assertThat(response.parentId()).isEqualTo(1L);

        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).findByParentCategory_IdAndName(1L, "Child Category");
    }

    @Test
    void testRegisterCategory_Failure_ParentNotFound() {
        // Given
        CategoryCreateRequest request = new CategoryCreateRequest(1L, "Child Category");

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> categoryService.registerCategory(request))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("부모 카테고리가 존재하지 않습니다.");

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testRegisterCategory_Failure_CategoryAlreadyExists() {
        // Given
        Category parentCategory = new Category();
        parentCategory.setId(1L);
        parentCategory.setName("Parent Category");

        CategoryCreateRequest request = new CategoryCreateRequest(1L, "Existing Category");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.findByParentCategory_IdAndName(1L, "Existing Category"))
                .thenReturn(Optional.of(new Category()));

        // When / Then
        assertThatThrownBy(() -> categoryService.registerCategory(request))
                .isInstanceOf(CategoryAlreadyExistsException.class)
                .hasMessageContaining("이미 존재하는 카테고리입니다.");

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).findByParentCategory_IdAndName(1L, "Existing Category");
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testGetCategory_SuccessWithParent() {
        // Given
        Long categoryId = 1L;
        Category parentCategory = new Category();
        parentCategory.setId(2L);
        parentCategory.setName("Parent Category");

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Child Category");
        category.setParentCategory(parentCategory);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When
        CategoryDetailResponse response = categoryService.getCategory(categoryId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(categoryId);
        assertThat(response.parentId()).isEqualTo(2L);
        assertThat(response.name()).isEqualTo("Child Category");

        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void testGetCategories_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        Category parentCategory = new Category();
        parentCategory.setId(1L);
        parentCategory.setName("Parent Category");

        Category childCategory = new Category();
        childCategory.setId(2L);
        childCategory.setName("Child Category");
        childCategory.setParentCategory(parentCategory);

        Page<Category> mockPage = new PageImpl<>(List.of(parentCategory, childCategory));
        when(categoryRepository.findAllBy(pageable)).thenReturn(mockPage);

        // When
        Page<CategoryDetailResponse> responsePage = categoryService.getCategories(pageable);

        // Then
        assertThat(responsePage).isNotNull();
        assertThat(responsePage.getTotalElements()).isEqualTo(2);

        CategoryDetailResponse parentResponse = responsePage.getContent().get(0);
        assertThat(parentResponse.id()).isEqualTo(1L);
        assertThat(parentResponse.parentId()).isNull();
        assertThat(parentResponse.name()).isEqualTo("Parent Category");

        CategoryDetailResponse childResponse = responsePage.getContent().get(1);
        assertThat(childResponse.id()).isEqualTo(2L);
        assertThat(childResponse.parentId()).isEqualTo(1L);
        assertThat(childResponse.name()).isEqualTo("Child Category");

        verify(categoryRepository, times(1)).findAllBy(pageable);
    }

    @Test
    void testGetCategoryPaths() {
        // Given
        Category rootCategory = new Category();
        rootCategory.setId(1L);
        rootCategory.setName("Root Category");

        Category subCategory1 = new Category();
        subCategory1.setId(2L);
        subCategory1.setName("Sub Category 1");
        subCategory1.setParentCategory(rootCategory);

        Category subCategory2 = new Category();
        subCategory2.setId(3L);
        subCategory2.setName("Sub Category 2");
        subCategory2.setParentCategory(subCategory1);

        Category isolatedCategory = new Category();
        isolatedCategory.setId(4L);
        isolatedCategory.setName("Isolated Category");

        when(categoryRepository.findAll()).thenReturn(List.of(rootCategory, subCategory1, subCategory2, isolatedCategory));

        // When
        List<String> result = categoryService.getCategoryPaths();

        // Then
        assertThat(result).containsExactlyInAnyOrder(
                "Root Category",
                "Root Category>Sub Category 1",
                "Root Category>Sub Category 1>Sub Category 2",
                "Isolated Category"
        );

        verify(categoryRepository, times(1)).findAll();
    }
}

