package com.marketplace.service;

import com.marketplace.dto.CartItem;
import com.marketplace.model.Cart;
import com.marketplace.model.CartItemEntity;
import com.marketplace.model.Product;
import com.marketplace.model.User;
import com.marketplace.repository.CartRepository;
import com.marketplace.repository.ProductRepository;
import com.marketplace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Управляет товарами в корзине пользователя.
 * Для авторизованных пользователей корзина сохраняется в БД.
 */
@Service
@SessionScope
@RequiredArgsConstructor
public class CartService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    private List<CartItem> items = new ArrayList<>();

    /**
     * Добавляет товар в корзину или увеличивает его количество.
     *
     * @param productId идентификатор товара
     * @param quantity количество товара
     */
    @Transactional
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

        saveToDatabase();
    }

    /**
     * Удаляет товар из корзины.
     *
     * @param productId идентификатор товара
     */
    @Transactional
    public void removeItem(Long productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
        saveToDatabase();
    }

    /**
     * Обновляет количество товара в корзине.
     *
     * @param productId идентификатор товара
     * @param quantity новое количество
     */
    @Transactional
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

        saveToDatabase();
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
    @Transactional
    public void clearCart() {
        items.clear();

        User user = getCurrentUser();
        if (user != null) {
            cartRepository.findByUser(user).ifPresent(cartRepository::delete);
        }
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

    /**
     * Загружает корзину из БД для текущего пользователя.
     * Вызывается после успешного логина.
     */
    @Transactional
    public void loadFromDatabase() {
        User user = getCurrentUser();
        if (user == null) {
            return;
        }

        Optional<Cart> cartOpt = cartRepository.findByUser(user);
        if (cartOpt.isEmpty()) {
            return;
        }

        items.clear();

        for (CartItemEntity entity : cartOpt.get().getItems()) {
            CartItem item = new CartItem();
            item.setProductId(entity.getProduct().getId());
            item.setProductName(entity.getProduct().getName());
            item.setQuantity(entity.getQuantity());
            item.setPrice(entity.getProduct().getPrice().doubleValue());

            items.add(item);
        }
    }

    /**
     * Сохраняет текущую корзину в БД.
     */
    @Transactional
    protected void saveToDatabase() {
        User user = getCurrentUser();
        if (user == null) {
            return;
        }

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        cart.getItems().clear();

        for (CartItem item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Товар не найден"));

            CartItemEntity entity = new CartItemEntity();
            entity.setCart(cart);
            entity.setProduct(product);
            entity.setQuantity(item.getQuantity());

            cart.getItems().add(entity);
        }

        cartRepository.save(cart);
    }

    /**
     * Возвращает текущего авторизованного пользователя или null.
     */
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return null;
        }

        return userRepository.findByEmail(auth.getName()).orElse(null);
    }
}