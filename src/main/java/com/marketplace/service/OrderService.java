package com.marketplace.service;

import com.marketplace.dto.CartItem;
import com.marketplace.dto.CheckoutRequest;
import com.marketplace.model.Order;
import com.marketplace.model.OrderItem;
import com.marketplace.model.OrderStatus;
import com.marketplace.model.Product;
import com.marketplace.model.User;
import com.marketplace.repository.OrderRepository;
import com.marketplace.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Управляет заказами пользователя.
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    /**
     * Создаёт заказ на основе корзины пользователя.
     *
     * @param user покупатель
     * @param request данные оформления
     * @param cartItems товары из корзины
     * @return созданный заказ
     */
    @Transactional
    public Order createOrder(User user, CheckoutRequest request, List<CartItem> cartItems) {
        Order order = new Order();
        order.setBuyer(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Товар не найден"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());

            order.getItems().add(orderItem);

            totalPrice = totalPrice.add(
                    product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))
            );
        }

        order.setTotalPrice(totalPrice);

        return orderRepository.save(order);
    }

    /**
     * Возвращает все заказы пользователя.
     *
     * @param user покупатель
     * @return список заказов
     */
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByBuyerOrderByOrderDateDesc(user);
    }
}