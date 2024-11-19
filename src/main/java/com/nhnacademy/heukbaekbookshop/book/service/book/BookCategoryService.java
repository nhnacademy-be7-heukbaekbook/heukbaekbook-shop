package com.nhnacademy.heukbaekbookshop.book.service.book;

import com.nhnacademy.heukbaekbookshop.book.domain.BookCategory;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookCategoryRepository;
import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.category.dto.response.CategorySummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookCategoryService {

    private final BookCategoryRepository bookCategoryRepository;

    public List<CategorySummaryResponse> findBookCategoriesByBookId(Long bookId) {
        List<BookCategory> bookCategories = bookCategoryRepository.findBookCategoriesByBookId(bookId);
        return bookCategories.stream()
                .map(bookCategory -> toCategorySummaryResponse(bookCategory.getCategory()))
                .collect(Collectors.toList());
    }

    private CategorySummaryResponse toCategorySummaryResponse(Category category) {
        List<CategorySummaryResponse> subCategories = category.getSubCategories().stream()
                .map(this::toCategorySummaryResponse)
                .collect(Collectors.toList());
        return new CategorySummaryResponse(category.getId(), category.getName(), subCategories);
    }
}
