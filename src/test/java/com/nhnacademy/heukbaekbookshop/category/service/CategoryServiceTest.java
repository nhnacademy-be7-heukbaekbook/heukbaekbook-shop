package com.nhnacademy.heukbaekbookshop.category.service;

import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.category.dto.request.CategoryCreateRequest;
import com.nhnacademy.heukbaekbookshop.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.heukbaekbookshop.category.dto.response.*;
import com.nhnacademy.heukbaekbookshop.category.exception.CategoryAlreadyExistsException;
import com.nhnacademy.heukbaekbookshop.category.exception.CategoryNotFoundException;
import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category parentCategory;
    private Category subCategory;

    @BeforeEach
    public void setUp() {
        parentCategory = new Category(null, "Parent Category");
        parentCategory.setId(1L);

        subCategory = new Category(parentCategory, "Sub Category");
        subCategory.setId(2L);
    }

    // 상위 카테고리 없이 카테고리 등록 성공
    @Test
    public void testRegisterCategory_SuccessWithoutParent() {
        // Given
        CategoryCreateRequest request = new CategoryCreateRequest(null, "New Category");
        when(categoryRepository.findByParentCategory_IdAndName(null, "New Category"))
                .thenReturn(Optional.empty());

        Category newCategory = new Category(null, "New Category");
        newCategory.setId(3L);
        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);

        // When
        CategoryCreateResponse response = categoryService.registerCategory(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.parentId()).isNull();
        assertThat(response.name()).isEqualTo("New Category");
        verify(categoryRepository).findByParentCategory_IdAndName(null, "New Category");
        verify(categoryRepository).save(any(Category.class));
    }

    // 상위 카테고리와 함께 카테고리 등록 성공
    @Test
    public void testRegisterCategory_SuccessWithParent() {
        // Given
        CategoryCreateRequest request = new CategoryCreateRequest(1L, "New Sub Category");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.findByParentCategory_IdAndName(1L, "New Sub Category"))
                .thenReturn(Optional.empty());

        Category newCategory = new Category(parentCategory, "New Sub Category");
        newCategory.setId(4L);
        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);

        // When
        CategoryCreateResponse response = categoryService.registerCategory(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.parentId()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("New Sub Category");
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).findByParentCategory_IdAndName(1L, "New Sub Category");
        verify(categoryRepository).save(any(Category.class));
    }

    // 이미 존재하는 카테고리 등록 시 예외 발생
    @Test
    public void testRegisterCategory_CategoryAlreadyExists() {
        // Given
        CategoryCreateRequest request = new CategoryCreateRequest(null, "Existing Category");
        when(categoryRepository.findByParentCategory_IdAndName(null, "Existing Category"))
                .thenReturn(Optional.of(new Category(null, "Existing Category")));

        // When & Then
        assertThatThrownBy(() -> categoryService.registerCategory(request))
                .isInstanceOf(CategoryAlreadyExistsException.class)
                .hasMessage("이미 존재하는 카테고리입니다.");
        verify(categoryRepository).findByParentCategory_IdAndName(null, "Existing Category");
        verify(categoryRepository, never()).save(any(Category.class));
    }

    // 존재하지 않는 부모 카테고리로 카테고리 등록 시 예외 발생
    @Test
    public void testRegisterCategory_ParentCategoryNotFound() {
        // Given
        CategoryCreateRequest request = new CategoryCreateRequest(99L, "New Category");
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> categoryService.registerCategory(request))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("부모 카테고리가 존재하지 않습니다.");
        verify(categoryRepository).findById(99L);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    // 카테고리 업데이트 성공
    @Test
    public void testUpdateCategory_Success() {
        // Given
        Long categoryId = 1L;
        Long parentCategoryId = 2L;
        String newName = "Updated Category";

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Old Category");

        Category parentCategory = new Category();
        parentCategory.setId(parentCategoryId);
        parentCategory.setName("Parent Category");

        CategoryUpdateRequest request = new CategoryUpdateRequest(parentCategoryId, "Updated Category");
        when(categoryRepository.findById(parentCategoryId)).thenReturn(Optional.of(parentCategory));


        // When
        CategoryUpdateResponse response = categoryService.updateCategory(2L, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.parentId()).isEqualTo(2L);
        assertThat(response.name()).isEqualTo("Updated Category");
    }

    // 존재하지 않는 카테고리 업데이트 시 예외 발생
    @Test
    public void testUpdateCategory_CategoryNotFound() {
        // Given
        CategoryUpdateRequest request = new CategoryUpdateRequest(null, "Updated Category");
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> categoryService.updateCategory(99L, request))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("존재하지 않는 카테고리입니다.");
        verify(categoryRepository).findById(99L);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    // 존재하지 않는 부모 카테고리로 업데이트 시 예외 발생
    @Test
    public void testUpdateCategory_ParentCategoryNotFound() {
        // Given
        CategoryUpdateRequest request = new CategoryUpdateRequest(99L, "Updated Category");
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(subCategory));
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> categoryService.updateCategory(2L, request))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("부모 카테고리가 존재하지 않습니다.");
        verify(categoryRepository).findById(2L);
        verify(categoryRepository).findById(99L);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    // 카테고리 삭제 성공
    @Test
    public void testDeleteCategory_Success() {
        // Given
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(subCategory));

        // When
        CategoryDeleteResponse response = categoryService.deleteCategory(2L);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.message()).isEqualTo("카테고리가 정상적으로 삭제되었습니다.");
        verify(categoryRepository).findById(2L);
        verify(categoryRepository).deleteById(2L);
    }

    // 존재하지 않는 카테고리 삭제 시 예외 발생
    @Test
    public void testDeleteCategory_CategoryNotFound() {
        // Given
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> categoryService.deleteCategory(99L))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("존재하지 않는 카테고리 입니다.");
        verify(categoryRepository).findById(99L);
        verify(categoryRepository, never()).deleteById(anyLong());
    }

    @Test
    void getCategory_ShouldReturnCategoryDetailResponse() {
        // Given
        Long categoryId = 1L;
        Category parentCategory = new Category();
        parentCategory.setId(0L);
        parentCategory.setName("Parent Category");

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Child Category");
        category.setParentCategory(parentCategory);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When
        CategoryDetailResponse response = categoryService.getCategory(categoryId);

        // Then
        assertThat(response)
                .isNotNull()
                .extracting(CategoryDetailResponse::id, CategoryDetailResponse::parentId, CategoryDetailResponse::name)
                .containsExactly(categoryId, parentCategory.getId(), "Child Category");

        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void getCategory_ShouldThrowExceptionWhenNotFound() {
        // Given
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> categoryService.getCategory(categoryId))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("존재하지 않는 카테고리입니다.");

        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void getCategories_ShouldReturnPaginatedCategoryResponses() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        Category parentCategory = new Category();
        parentCategory.setId(0L);
        parentCategory.setName("Parent Category");

        Category category = new Category();
        category.setId(1L);
        category.setName("Child Category");
        category.setParentCategory(parentCategory);

        Page<Category> categoryPage = new PageImpl<>(List.of(category));
        when(categoryRepository.findAllBy(pageable)).thenReturn(categoryPage);

        // When
        Page<CategoryDetailResponse> responses = categoryService.getCategories(pageable);

        // Then
        assertThat(responses)
                .isNotNull()
                .hasSize(1);

        assertThat(responses.getContent().getFirst())
                .extracting(CategoryDetailResponse::id, CategoryDetailResponse::parentId, CategoryDetailResponse::name)
                .containsExactly(category.getId(), parentCategory.getId(), "Child Category");

        verify(categoryRepository, times(1)).findAllBy(pageable);
    }

    @Test
    void getCategoryPaths_ShouldReturnCategoryPaths() {
        // Given
        Category rootCategory = new Category();
        rootCategory.setId(1L);
        rootCategory.setName("Root");

        Category childCategory = new Category();
        childCategory.setId(2L);
        childCategory.setName("Child");
        childCategory.setParentCategory(rootCategory);

        Category subChildCategory = new Category();
        subChildCategory.setId(3L);
        subChildCategory.setName("SubChild");
        subChildCategory.setParentCategory(childCategory);

        when(categoryRepository.findAll()).thenReturn(List.of(rootCategory, childCategory, subChildCategory));

        // When
        List<String> categoryPaths = categoryService.getCategoryPaths();

        // Then
        assertThat(categoryPaths)
                .isNotNull()
                .containsExactlyInAnyOrder("Root", "Root>Child", "Root>Child>SubChild");

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void getParentCategories_Success() {
        // given
        Category parent1 = Category.createRootCategory("최상위1");
        Category parent2 = Category.createRootCategory("최상위2");
        Category parent3 = Category.createRootCategory("최상위3");

        Category category1 = Category.createSubCategory("상위1", parent1);
        Category category2 = Category.createSubCategory("상위2", parent2);
        Category category3 = Category.createSubCategory("상위3", parent3);

        Category.createSubCategory("하위1", category1);
        Category.createSubCategory("하위2", category1);
        when(categoryRepository.findTopCategories()).thenReturn(List.of(parent1, parent2, parent3));


        // when
        List<CategorySummaryResponse> parentCategories = categoryService.getTopCategories();

        // then
        assertThat(parentCategories).isNotNull();
        assertThat(parentCategories.size()).isEqualTo(3);
    }
}
