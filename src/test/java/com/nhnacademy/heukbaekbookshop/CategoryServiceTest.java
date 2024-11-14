//package com.nhnacademy.heukbaekbookshop;
//
//import com.nhnacademy.heukbaekbookshop.category.domain.Category;
//import com.nhnacademy.heukbaekbookshop.category.dto.request.CategoryCreateRequest;
//import com.nhnacademy.heukbaekbookshop.category.dto.request.CategoryUpdateRequest;
//import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryCreateResponse;
//import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryDeleteResponse;
//import com.nhnacademy.heukbaekbookshop.category.dto.response.CategoryUpdateResponse;
//import com.nhnacademy.heukbaekbookshop.category.exception.CategoryAlreadyExistsException;
//import com.nhnacademy.heukbaekbookshop.category.exception.CategoryNotFoundException;
//import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepository;
//import com.nhnacademy.heukbaekbookshop.category.service.CategoryService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class CategoryServiceTest {
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @InjectMocks
//    private CategoryService categoryService;
//
//    private Category parentCategory;
//    private Category subCategory;
//
//    @BeforeEach
//    public void setUp() {
//        parentCategory = new Category(null, "Parent Category");
//        parentCategory.setId(1L);
//
//        subCategory = new Category(parentCategory, "Sub Category");
//        subCategory.setId(2L);
//    }
//
//    // 상위 카테고리 없이 카테고리 등록 성공
//    @Test
//    public void testRegisterCategory_SuccessWithoutParent() {
//        // Given
//        CategoryCreateRequest request = new CategoryCreateRequest(null, "New Category");
//        when(categoryRepository.findByParentCategory_IdAndName(null, "New Category"))
//                .thenReturn(Optional.empty());
//
//        Category newCategory = new Category(null, "New Category");
//        newCategory.setId(3L);
//        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);
//
//        // When
//        CategoryCreateResponse response = categoryService.registerCategory(request);
//
//        // Then
//        assertThat(response).isNotNull();
//        assertThat(response.parentId()).isNull();
//        assertThat(response.name()).isEqualTo("New Category");
//        verify(categoryRepository).findByParentCategory_IdAndName(null, "New Category");
//        verify(categoryRepository).save(any(Category.class));
//    }
//
//    // 상위 카테고리와 함께 카테고리 등록 성공
//    @Test
//    public void testRegisterCategory_SuccessWithParent() {
//        // Given
//        CategoryCreateRequest request = new CategoryCreateRequest(1L, "New Sub Category");
//        when(categoryRepository.findById(1L)).thenReturn(Optional.of(parentCategory));
//        when(categoryRepository.findByParentCategory_IdAndName(1L, "New Sub Category"))
//                .thenReturn(Optional.empty());
//
//        Category newCategory = new Category(parentCategory, "New Sub Category");
//        newCategory.setId(4L);
//        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);
//
//        // When
//        CategoryCreateResponse response = categoryService.registerCategory(request);
//
//        // Then
//        assertThat(response).isNotNull();
//        assertThat(response.parentId()).isEqualTo(1L);
//        assertThat(response.name()).isEqualTo("New Sub Category");
//        verify(categoryRepository).findById(1L);
//        verify(categoryRepository).findByParentCategory_IdAndName(1L, "New Sub Category");
//        verify(categoryRepository).save(any(Category.class));
//    }
//
//    // 이미 존재하는 카테고리 등록 시 예외 발생
//    @Test
//    public void testRegisterCategory_CategoryAlreadyExists() {
//        // Given
//        CategoryCreateRequest request = new CategoryCreateRequest(null, "Existing Category");
//        when(categoryRepository.findByParentCategory_IdAndName(null, "Existing Category"))
//                .thenReturn(Optional.of(new Category(null, "Existing Category")));
//
//        // When & Then
//        assertThatThrownBy(() -> categoryService.registerCategory(request))
//                .isInstanceOf(CategoryAlreadyExistsException.class)
//                .hasMessage("이미 존재하는 카테고리입니다.");
//        verify(categoryRepository).findByParentCategory_IdAndName(null, "Existing Category");
//        verify(categoryRepository, never()).save(any(Category.class));
//    }
//
//    // 존재하지 않는 부모 카테고리로 카테고리 등록 시 예외 발생
//    @Test
//    public void testRegisterCategory_ParentCategoryNotFound() {
//        // Given
//        CategoryCreateRequest request = new CategoryCreateRequest(99L, "New Category");
//        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThatThrownBy(() -> categoryService.registerCategory(request))
//                .isInstanceOf(CategoryNotFoundException.class)
//                .hasMessage("부모 카테고리가 존재하지 않습니다.");
//        verify(categoryRepository).findById(99L);
//        verify(categoryRepository, never()).save(any(Category.class));
//    }
//
//    // 카테고리 업데이트 성공
//    @Test
//    public void testUpdateCategory_Success() {
//        // Given
//        CategoryUpdateRequest request = new CategoryUpdateRequest(null, "Updated Category");
//        when(categoryRepository.findById(2L)).thenReturn(Optional.of(subCategory));
//
//        // When
//        CategoryUpdateResponse response = categoryService.updateCategory(2L, request);
//
//        // Then
//        assertThat(response).isNotNull();
//        assertThat(response.parentId()).isNull();
//        assertThat(response.name()).isEqualTo("Updated Category");
//        verify(categoryRepository).findById(2L);
//        verify(categoryRepository).save(subCategory);
//    }
//
//    // 존재하지 않는 카테고리 업데이트 시 예외 발생
//    @Test
//    public void testUpdateCategory_CategoryNotFound() {
//        // Given
//        CategoryUpdateRequest request = new CategoryUpdateRequest(null, "Updated Category");
//        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThatThrownBy(() -> categoryService.updateCategory(99L, request))
//                .isInstanceOf(CategoryNotFoundException.class)
//                .hasMessage("존재하지 않는 카테고리입니다.");
//        verify(categoryRepository).findById(99L);
//        verify(categoryRepository, never()).save(any(Category.class));
//    }
//
//    // 존재하지 않는 부모 카테고리로 업데이트 시 예외 발생
//    @Test
//    public void testUpdateCategory_ParentCategoryNotFound() {
//        // Given
//        CategoryUpdateRequest request = new CategoryUpdateRequest(99L, "Updated Category");
//        when(categoryRepository.findById(2L)).thenReturn(Optional.of(subCategory));
//        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThatThrownBy(() -> categoryService.updateCategory(2L, request))
//                .isInstanceOf(CategoryNotFoundException.class)
//                .hasMessage("부모 카테고리가 존재하지 않습니다.");
//        verify(categoryRepository).findById(2L);
//        verify(categoryRepository).findById(99L);
//        verify(categoryRepository, never()).save(any(Category.class));
//    }
//
//    // 카테고리 삭제 성공
//    @Test
//    public void testDeleteCategory_Success() {
//        // Given
//        when(categoryRepository.findById(2L)).thenReturn(Optional.of(subCategory));
//
//        // When
//        CategoryDeleteResponse response = categoryService.deleteCategory(2L);
//
//        // Then
//        assertThat(response).isNotNull();
//        assertThat(response.message()).isEqualTo("카테고리가 정상적으로 삭제되었습니다.");
//        verify(categoryRepository).findById(2L);
//        verify(categoryRepository).deleteById(2L);
//    }
//
//    // 존재하지 않는 카테고리 삭제 시 예외 발생
//    @Test
//    public void testDeleteCategory_CategoryNotFound() {
//        // Given
//        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThatThrownBy(() -> categoryService.deleteCategory(99L))
//                .isInstanceOf(CategoryNotFoundException.class)
//                .hasMessage("존재하지 않는 카테고리 입니다.");
//        verify(categoryRepository).findById(99L);
//        verify(categoryRepository, never()).deleteById(anyLong());
//    }
//}
