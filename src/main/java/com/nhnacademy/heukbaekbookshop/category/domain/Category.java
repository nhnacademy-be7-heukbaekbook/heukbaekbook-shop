package com.nhnacademy.heukbaekbookshop.category.domain;

import com.nhnacademy.heukbaekbookshop.book.domain.BookCategory;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.CategoryCoupon;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @NotNull
    @Length(min = 1, max = 100)
    @Column(name = "category_name")
    @Setter
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookCategory> bookCategories = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CategoryCoupon> categoryCoupons = new HashSet<>();

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
        if (parentCategory != null) {
            parentCategory.getSubCategories().add(this);
        }
    }

    public void addBookCategory(BookCategory bookCategory) {
        this.bookCategories.add(bookCategory);
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
