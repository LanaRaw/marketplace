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

@Service
@SessionScope  // ← Важно! Корзина будет жить в сессии
@RequiredArgsConstructor
public class CartService {

    private final ProductRepository productRepository;

    // Список товаров в корзине (хранится в сессии)
    private List<CartItem> items = new ArrayList<>();

    // Добавить товар в корзину
    public void addItem(Long productId, Integer quantity) {
        // Проверяем, есть ли уже такой товар в корзине
        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            // Если есть — увеличиваем количество
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            // Если нет — создаем новый
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

    // Удалить товар из корзины
    public void removeItem(Long productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
    }

    // Обновить количество товара
    public void updateQuantity(Long productId, Integer quantity) {
        items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));
    }

    // Получить все товары в корзине
    public List<CartItem> getItems() {
        return items;
    }

    // Очистить корзину
    public void clearCart() {
        items.clear();
    }

    // Получить общую сумму корзины
    public Double getTotalAmount() {
        return items.stream()
                .mapToDouble(CartItem::getTotal)
                .sum();
    }

    // Получить количество товаров в корзине
    public int getTotalItems() {
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}