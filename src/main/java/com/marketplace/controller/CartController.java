package com.marketplace.controller;

import com.marketplace.dto.CartItem;
import com.marketplace.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Страница корзины
    @GetMapping
    public String viewCart(Model model) {
        List<CartItem> items = cartService.getItems();
        model.addAttribute("items", items);
        model.addAttribute("total", cartService.getTotalAmount());
        model.addAttribute("totalItems", cartService.getTotalItems());
        return "cart";
    }

    // Добавить товар в корзину
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") Integer quantity) {
        cartService.addItem(productId, quantity);
        return "redirect:/cart";  // После добавления переходим в корзину
    }

    // Удалить товар из корзины
    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cartService.removeItem(productId);
        return "redirect:/cart";
    }

    // Обновить количество
    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long productId,
                                 @RequestParam Integer quantity) {
        if (quantity <= 0) {
            cartService.removeItem(productId);
        } else {
            cartService.updateQuantity(productId, quantity);
        }
        return "redirect:/cart";
    }

    // Очистить корзину
    @PostMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }
}