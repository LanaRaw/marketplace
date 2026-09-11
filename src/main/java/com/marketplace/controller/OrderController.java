package com.marketplace.controller;

import com.marketplace.dto.CartItem;
import com.marketplace.dto.CheckoutRequest;
import com.marketplace.model.Order;
import com.marketplace.model.User;
import com.marketplace.repository.UserRepository;
import com.marketplace.service.CartService;
import com.marketplace.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Обрабатывает запросы, связанные с оформлением и историей заказов.
 */
@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final UserRepository userRepository;

    /**
     * Показывает страницу оформления заказа.
     */
    @GetMapping("/checkout")
    public String showCheckoutPage(Model model) {
        List<CartItem> items = cartService.getItems();

        if (items.isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("items", items);
        model.addAttribute("total", cartService.getTotalAmount());
        model.addAttribute("checkoutRequest", new CheckoutRequest());
        model.addAttribute("title", "Оформление заказа");

        return "order/checkout";
    }

    /**
     * Обрабатывает оформление заказа.
     */
    @PostMapping("/checkout")
    public String processCheckout(
            @Valid @ModelAttribute("checkoutRequest") CheckoutRequest request,
            BindingResult bindingResult,
            Authentication authentication,
            Model model) {

        List<CartItem> items = cartService.getItems();

        if (items.isEmpty()) {
            return "redirect:/cart";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("items", items);
            model.addAttribute("total", cartService.getTotalAmount());
            model.addAttribute("title", "Оформление заказа");
            return "order/checkout";
        }

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Order order = orderService.createOrder(user, request, items);

        cartService.clearCart();

        return "redirect:/orders/confirmation/" + order.getId();
    }

    /**
     * Показывает страницу подтверждения заказа.
     */
    @GetMapping("/confirmation/{id}")
    public String orderConfirmation(@PathVariable Long id, Model model) {
        model.addAttribute("orderId", id);
        model.addAttribute("title", "Заказ оформлен");
        return "order/confirmation";
    }

    /**
     * Показывает историю заказов пользователя.
     */
    @GetMapping("/history")
    public String orderHistory(Authentication authentication, Model model) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        List<Order> orders = orderService.getOrdersByUser(user);
        model.addAttribute("orders", orders);
        model.addAttribute("title", "Мои заказы");

        return "order/history";
    }
}