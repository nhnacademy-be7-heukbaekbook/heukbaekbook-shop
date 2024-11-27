package com.nhnacademy.heukbaekbookshop.book.service.book;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.domain.BookCategory;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookCategoryNotFoundException;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookCategoryRepository;
import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.category.dto.response.ParentCategoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BookCategoryServiceTest {

    @InjectMocks
    private BookCategoryService bookCategoryService;

    @Mock
    private BookCategoryRepository bookCategoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindBookCategoriesByBookId_Success() {
        // Given
        Long bookId = 1L;
        Category category = new Category();
        category.setId(2L);
        category.setName("SubCategory");

        Category parentCategory = new Category();
        parentCategory.setId(1L);
        parentCategory.setName("ParentCategory");

        category.setParentCategory(parentCategory);

        Book book = new Book();
        book.setId(bookId);

        BookCategory bookCategory = new BookCategory(book, category);

        when(bookCategoryRepository.findBookCategoriesByBookId(bookId)).thenReturn(Optional.of(bookCategory));

        // When
        List<ParentCategoryResponse> result = bookCategoryService.findBookCategoriesByBookId(bookId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(1L);
        assertThat(result.get(0).name()).isEqualTo("ParentCategory");
        assertThat(result.get(1).id()).isEqualTo(2L);
        assertThat(result.get(1).name()).isEqualTo("SubCategory");

        verify(bookCategoryRepository, times(1)).findBookCategoriesByBookId(bookId);
    }


    @Test
    void testFindBookCategoriesByBookId_NotFound() {
        // Given
        Long bookId = 1L;
        when(bookCategoryRepository.findBookCategoriesByBookId(bookId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(BookCategoryNotFoundException.class, () -> bookCategoryService.findBookCategoriesByBookId(bookId));
        verify(bookCategoryRepository, times(1)).findBookCategoriesByBookId(bookId);
    }
}
