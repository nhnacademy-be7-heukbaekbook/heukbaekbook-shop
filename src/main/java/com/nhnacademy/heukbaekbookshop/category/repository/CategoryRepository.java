package com.nhnacademy.heukbaekbookshop.category.repository;

import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {
    Optional<Category> findByName(String name);
  
    @Query("SELECT c FROM Category c WHERE c.name = :name AND " +
            "((:parentId IS NULL AND c.parentCategory IS NULL) OR " +
            "(:parentId IS NOT NULL AND c.parentCategory.id = :parentId))")
    Optional<Category> findByParentCategory_IdAndName(Long parentId, String name);

    Optional<Category> findByNameAndParentCategory(String trimmedName, Category parentCategory);

    Page<Category> findAllBy(Pageable pageable);

    @Query(value = "    WITH RECURSIVE category_hierarchy AS (\n" +
                   "        SELECT category_id\n" +
                   "        FROM categories\n" +
                   "        WHERE category_id = :categoryId\n" +
                   "        UNION ALL\n" +
                   "        SELECT c.category_id\n" +
                   "        FROM categories c\n" +
                   "        INNER JOIN category_hierarchy ch ON c.parent_category_id = ch.category_id\n" +
                   "    )\n" +
                   "    SELECT ch.category_id\n" +
                   "    FROM category_hierarchy ch\n", nativeQuery = true)
    List<Long> findSubCategoryIdsByCategoryId(@Param("categoryId") Long categoryId);

    @Query(value = "    WITH RECURSIVE category_hierarchy AS (" +
            "        SELECT category_id, parent_category_id " +
            "        FROM categories " +
            "        WHERE category_id IN (:categoryIds) " +
            "        UNION ALL " +
            "        SELECT c.category_id, c.parent_category_id " +
            "        FROM categories c " +
            "        INNER JOIN category_hierarchy ch ON c.category_id = ch.parent_category_id " +
            "    ) " +
            "    SELECT DISTINCT ch.category_id " +
            "    FROM category_hierarchy ch", nativeQuery = true)
    List<Long> findParentCategoryIdsByCategoryIds(@Param("categoryIds") Set<Long> categoryIds);

}
