//package com.nhnacademy.heukbaekbookshop.book.repository.book.impl;
//
//import com.nhnacademy.heukbaekbookshop.book.domain.*;
//import com.nhnacademy.heukbaekbookshop.book.repository.book.BookCategoryRepository;
//import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
//import com.nhnacademy.heukbaekbookshop.category.domain.Category;
//import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//// import org.springframework.boot.test.context.SpringBootTest; // 필요한 경우 사용
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//class BookCategoryRepositoryImplTest {
//
//    @Autowired
//    private BookCategoryRepository bookCategoryRepository;
//
//    @Autowired
//    private BookRepository bookRepository;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @BeforeEach
//    void setUp() {
//        // 테스트 데이터 생성
//        // 카테고리 생성
//        Category category1 = new Category();
//        category1.setName("Category 1");
//        categoryRepository.save(category1);
//
//        Category category2 = new Category();
//        category2.setName("Category 2");
//        categoryRepository.save(category2);
//
//        // 도서 생성
//        Book book1 = new Book();
//        book1.setTitle("Book 1");
//        book1.setStatus(BookStatus.IN_STOCK);
//        bookRepository.save(book1);
//
//        Book book2 = new Book();
//        book2.setTitle("Book 2");
//        book2.setStatus(BookStatus.IN_STOCK);
//        bookRepository.save(book2);
//
//        Book book3 = new Book();
//        book3.setTitle("Book 3");
//        book3.setStatus(BookStatus.OUT_OF_STOCK);
//        bookRepository.save(book3);
//
//        // BookCategory 생성
//        BookCategory bookCategory1 = new BookCategory(book1, category1);
//        bookCategoryRepository.save(bookCategory1);
//
//        BookCategory bookCategory2 = new BookCategory(book2, category1);
//        bookCategoryRepository.save(bookCategory2);
//
//        BookCategory bookCategory3 = new BookCategory(book3, category2);
//        bookCategoryRepository.save(bookCategory3);
//    }
//
//    @Test
//    void testFindByCategoryIds() {
//        // Given
//        List<Long> categoryIds = categoryRepository.findAll().stream()
//                .map(Category::getId)
//                .toList();
//        Pageable pageable = PageRequest.of(0, 10);
//
//        // When
//        Page<BookCategory> result = bookCategoryRepository.findByCategoryIds(categoryIds, pageable);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(3, result.getTotalElements());
//        List<BookCategory> content = result.getContent();
//        assertEquals(0, content.size());
//
//        for (BookCategory bc : content) {
//            assertEquals(BookStatus.IN_STOCK, bc.getBook().getStatus());
//        }
//    }
//
//    @Test
//    void testFindBookCategoriesByBookId() {
//        // Given
//        Long bookId = bookRepository.findAll().get(0).getId();
//
//        // When
//        Optional<BookCategory> result = bookCategoryRepository.findBookCategoriesByBookId(bookId);
//
//        // Then
//        assertTrue(result.isPresent());
//        assertEquals(bookId, result.get().getBook().getId());
//    }
//}
