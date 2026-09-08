package com.marketplace.service;

import com.marketplace.dto.CartItem;
import com.marketplace.model.Product;
import com.marketplace.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Управляет товарами в корзине пользователя.
 */
@Service
@SessionScope
@RequiredArgsConstructor
public class CartService {

    private final ProductRepository productRepository;

    private List<CartItem> items = new ArrayList<>();

    /**
     * Добавляет товар в корзину или увеличивает его количество.
     *
     * @param productId идентификатор товара
     * @param quantity количество товара
     */
    public void addItem(Long productId, Integer quantity) {
        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Товар не найден"));

            CartItem newItem = new CartItem();
            newItem.setProductId(product.getId());
            newItem.setProductName(product.getName());
            newItem.setQuantity(quantity);
            newItem.setPrice(product.getPrice().doubleValue());

            items.add(newItem);
        }
    }

    /**
     * Удаляет товар из корзины.
     *
     * @param productId идентификатор товара
     */
    public void removeItem(Long productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
    }

    /**
     * Обновляет количество товара в корзине.
     *
     * @param productId идентификатор товара
     * @param quantity новое количество
     */
    public void updateQuantity(Long productId, Integer quantity) {
        boolean found = false;

        for (CartItem item : items) {
            if (item.getProductId().equals(productId)) {
                if (quantity <= 0) {
                    items.remove(item);
                } else {
                    item.setQuantity(quantity);
                }
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("⚠️ Товар с id " + productId + " не найден в корзине");
        }
    }

    /**
     * Возвращает все товары в корзине.
     *
     * @return список товаров
     */
    public List<CartItem> getItems() {
        return items;
    }

    /**
     * Очищает корзину.
     */
    public void clearCart() {
        items.clear();
    }

    /**
     * Возвращает общую стоимость товаров в корзине.
     *
     * @return общая сумма
     */
    public Double getTotalAmount() {
        return items.stream()
                .mapToDouble(CartItem::getTotal)
                .sum();
    }

    /**
     * Возвращает общее количество товаров в корзине.
     *
     * @return количество товаров
     */
    public int getTotalItems() {
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}