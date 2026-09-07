package com.marketplace.service;

import com.marketplace.model.Category;
import com.marketplace.model.Product;
import com.marketplace.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // Получить все активные товары с пагинацией
    public Page<Product> getActiveProducts(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return productRepository.findByIsActiveTrue(pageable);
    }

    // Поиск товаров по названию
    public Page<Product> searchProducts(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(query, pageable);
    }

    // Фильтр по категории
    public Page<Product> filterByCategory(Category category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return productRepository.findByCategoryAndIsActiveTrue(category, pageable);
    }

    // Поиск + фильтр по категории
    public Page<Product> searchAndFilter(String query, Category category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        if (category != null && query != null && !query.isEmpty()) {
            return productRepository.findByCategoryAndNameContainingIgnoreCaseAndIsActiveTrue(
                    category, query, pageable);
        } else if (category != null) {
            return productRepository.findByCategoryAndIsActiveTrue(category, pageable);
        } else if (query != null && !query.isEmpty()) {
            return productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(query, pageable);
        } else {
            return productRepository.findByIsActiveTrue(pageable);
        }
    }

    // Получить товар по ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Получить все активные категории
    public List<Category> getAllCategories() {
        return productRepository.findAllActiveCategories();
    }
}