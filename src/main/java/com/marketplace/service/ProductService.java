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

/**
 * Предоставляет операции для работы с товарами.
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Возвращает активные товары с пагинацией и сортировкой.
     *
     * @param page номер страницы
     * @param size количество товаров на странице
     * @param sortBy поле для сортировки
     * @return страница активных товаров
     */
    public Page<Product> getActiveProducts(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return productRepository.findByIsActiveTrue(pageable);
    }

    /**
     * Выполняет поиск активных товаров по названию.
     *
     * @param query поисковый запрос
     * @param page номер страницы
     * @param size количество товаров на странице
     * @return страница найденных товаров
     */
    public Page<Product> searchProducts(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(query, pageable);
    }

    /**
     * Возвращает активные товары выбранной категории.
     *
     * @param category категория товаров
     * @param page номер страницы
     * @param size количество товаров на странице
     * @return страница товаров категории
     */
    public Page<Product> filterByCategory(Category category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return productRepository.findByCategoryAndIsActiveTrue(category, pageable);
    }

    /**
     * Выполняет поиск товаров с учётом категории.
     *
     * @param query поисковый запрос
     * @param category категория товаров
     * @param page номер страницы
     * @param size количество товаров на странице
     * @return страница найденных товаров
     */
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

    /**
     * Находит товар по идентификатору.
     *
     * @param id идентификатор товара
     * @return товар, если найден
     */
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Возвращает все категории активных товаров.
     *
     * @return список категорий
     */
    public List<Category> getAllCategories() {
        return productRepository.findAllActiveCategories();
    }
}