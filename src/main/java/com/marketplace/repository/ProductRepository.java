package com.marketplace.repository;

import com.marketplace.model.Category;
import com.marketplace.model.Product;
import com.marketplace.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Находит все активные товары с пагинацией.
     *
     * @param pageable параметры пагинации
     * @return страница активных товаров
     */
    Page<Product> findByIsActiveTrue(Pageable pageable);

    /**
     * Выполняет поиск активных товаров по названию без учёта регистра.
     *
     * @param name название для поиска
     * @param pageable параметры пагинации
     * @return страница найденных товаров
     */
    Page<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String name, Pageable pageable);

    /**
     * Находит активные товары указанной категории с пагинацией.
     *
     * @param category категория товара
     * @param pageable параметры пагинации
     * @return страница товаров выбранной категории
     */
    Page<Product> findByCategoryAndIsActiveTrue(Category category, Pageable pageable);

    /**
     * Выполняет поиск активных товаров по названию и категории.
     *
     * @param category категория товара
     * @param name название для поиска
     * @param pageable параметры пагинации
     * @return страница найденных товаров
     */
    Page<Product> findByCategoryAndNameContainingIgnoreCaseAndIsActiveTrue(
            Category category, String name, Pageable pageable);

    /**
     * Находит все активные товары указанного продавца.
     *
     * @param seller продавец
     * @return список товаров продавца
     */
    List<Product> findBySellerAndIsActiveTrue(User seller);

    /**
     * Возвращает категории, используемые активными товарами.
     *
     * @return список уникальных категорий
     */
    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.isActive = true")
    List<Category> findAllActiveCategories();
}