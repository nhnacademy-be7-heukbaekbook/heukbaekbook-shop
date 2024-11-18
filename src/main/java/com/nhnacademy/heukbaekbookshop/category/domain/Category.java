package com.nhnacademy.heukbaekbookshop.category.domain;

import com.nhnacademy.heukbaekbookshop.book.domain.BookCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Category> subCategories = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @NotNull
    @Length(min = 1, max = 100)
    @Column(name = "category_name")
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookCategory> bookCategories = new HashSet<>();

    public void addBookCategory(BookCategory bookCategory) {
        this.bookCategories.add(bookCategory);
    }

    private void setCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
        parentCategory.getSubCategories().add(this);
    }

    public void removeBookCategory(BookCategory bookCategory) {
        this.bookCategories.remove(bookCategory);
    }

    public static Category createRootCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return category;
    }

    public static Category createSubCategory(String name, Category parentCategory) {
        Category subCategory = new Category();
        subCategory.setName(name);
        subCategory.setParentCategory(parentCategory);
        return subCategory;
    }

    @Builder
    public Category(Category parentCategory, String name) {
        this.parentCategory = parentCategory;
        this.name = name;
    }
}
