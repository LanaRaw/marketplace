package com.marketplace.repository;

import com.marketplace.model.Category;
import com.marketplace.model.Product;
import com.marketplace.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Все активные товары с пагинацией
    Page<Product> findByIsActiveTrue(Pageable pageable);

    // Поиск по названию (без учёта регистра) с пагинацией
    Page<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String name, Pageable pageable);

    // Фильтр по категории с пагинацией
    Page<Product> findByCategoryAndIsActiveTrue(Category category, Pageable pageable);

    // Поиск + категория одновременно
    Page<Product> findByCategoryAndNameContainingIgnoreCaseAndIsActiveTrue(
            Category category, String name, Pageable pageable);

    // Товары конкретного продавца
    List<Product> findBySellerAndIsActiveTrue(User seller);

    // Получить все категории (для фильтра)
    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.isActive = true")
    List<Category> findAllActiveCategories();
}