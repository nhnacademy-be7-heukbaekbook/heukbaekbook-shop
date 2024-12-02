package com.nhnacademy.heukbaekbookshop.book.service.book;

import com.nhnacademy.heukbaekbookshop.book.domain.BookCategory;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookCategoryNotFoundException;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookCategoryRepository;
import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.category.dto.response.ParentCategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCategoryService {

    private final BookCategoryRepository bookCategoryRepository;

    public List<ParentCategoryResponse> findBookCategoriesByBookId(Long bookId) {
        BookCategory bookCategory = bookCategoryRepository.findBookCategoriesByBookId(bookId)
                .orElseThrow(() -> new BookCategoryNotFoundException("bookCategory not found"));

        return getParentCategories(bookCategory.getCategory());
    }

    private List<ParentCategoryResponse> getParentCategories(Category category) {
        List<ParentCategoryResponse> parentCategoryResponses = new ArrayList<>();

        Category current = category;

        while (current != null) {
            parentCategoryResponses.add(new ParentCategoryResponse(current.getId(), current.getName()));
            current = current.getParentCategory();
        }

        return parentCategoryResponses.reversed();
    }
}
