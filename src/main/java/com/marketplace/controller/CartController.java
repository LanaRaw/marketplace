package com.marketplace.controller;

import com.marketplace.dto.CartItem;
import com.marketplace.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Обрабатывает запросы, связанные с корзиной пользователя.
 */
@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * Отображает содержимое корзины.
     *
     * @param model модель для передачи данных в представление
     * @return имя шаблона корзины
     */
    @GetMapping
    public String viewCart(Model model) {
        List<CartItem> items = cartService.getItems();
        model.addAttribute("items", items);
        model.addAttribute("total", cartService.getTotalAmount());
        model.addAttribute("totalItems", cartService.getTotalItems());
        return "cart";
    }

    /**
     * Добавляет товар в корзину.
     *
     * @param productId идентификатор товара
     * @param quantity количество товара
     * @return перенаправление на страницу корзины
     */
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") Integer quantity) {
        cartService.addItem(productId, quantity);
        return "redirect:/cart";
    }

    /**
     * Удаляет товар из корзины.
     *
     * @param productId идентификатор товара
     * @return перенаправление на страницу корзины
     */
    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cartService.removeItem(productId);
        return "redirect:/cart";
    }

    /**
     * Обновляет количество товара в корзине.
     *
     * Если новое количество меньше или равно нулю, товар удаляется из корзины.
     *
     * @param productId идентификатор товара
     * @param quantity новое количество товара
     * @return перенаправление на страницу корзины
     */
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

    /**
     * Полностью очищает корзину.
     *
     * @return перенаправление на страницу корзины
     */
    @PostMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }
}