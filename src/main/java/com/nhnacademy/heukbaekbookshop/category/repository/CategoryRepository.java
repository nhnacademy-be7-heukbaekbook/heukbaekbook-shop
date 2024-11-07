package com.nhnacademy.heukbaekbookshop.category.repository;

import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
  
    @Query("SELECT c FROM Category c WHERE c.name = :name AND " +
            "((:parentId IS NULL AND c.parentCategory IS NULL) OR " +
            "(:parentId IS NOT NULL AND c.parentCategory.id = :parentId))")
    Optional<Category> findByParentCategory_IdAndName(Long parentId, String name);

    Optional<Category> findByNameAndParentCategory(String trimmedName, Category parentCategory);

    Page<Category> findAllBy(Pageable pageable);
}
