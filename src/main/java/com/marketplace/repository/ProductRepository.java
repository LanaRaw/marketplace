package com.marketplace.repository;

import com.marketplace.model.Category;
import com.marketplace.model.Product;
import com.marketplace.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Найти все активные товары с пагинацией
    Page<Product> findByIsActiveTrue(Pageable pageable);

    // Поиск по названию (игнорируя регистр)
    Page<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String name, Pageable pageable);

    // Фильтр по категории
    Page<Product> findByCategoryAndIsActiveTrue(Category category, Pageable pageable);

    // Найти товары конкретного продавца
    List<Product> findBySellerAndIsActiveTrue(User seller);
}