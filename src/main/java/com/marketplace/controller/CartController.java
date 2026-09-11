package com.marketplace.controller;

import com.marketplace.dto.CartItem;
import com.marketplace.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
     * Если корзина в сессии пуста, но пользователь авторизован —
     * пытается загрузить корзину из базы данных.
     *
     * @param model модель для передачи данных в представление
     * @return имя шаблона корзины
     */
    @GetMapping
    public String viewCart(Model model) {
        if (cartService.getItems().isEmpty()) {
            cartService.loadFromDatabase();
        }

        List<CartItem> items = cartService.getItems();
        model.addAttribute("items", items);
        model.addAttribute("total", cartService.getTotalAmount());
        model.addAttribute("totalItems", cartService.getTotalItems());

        model.addAttribute("title", "Корзина");
        return "cart";
    }

    /**
     * Возвращает количество товаров в корзине (для AJAX-обновления бейджа).
     *
     * @return количество товаров
     */
    @GetMapping("/count")
    @ResponseBody
    public int getCartCount() {
        return cartService.getTotalItems();
    }

    /**
     * Добавляет товар в корзину.
     *
     * @param productId идентификатор товара
     * @param quantity количество товара
     * @param redirectAttributes атрибуты для flash-сообщения
     * @return перенаправление на страницу корзины
     */
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") Integer quantity,
                            RedirectAttributes redirectAttributes) {
        cartService.addItem(productId, quantity);
        redirectAttributes.addFlashAttribute("success", "Товар добавлен в корзину");
        return "redirect:/cart";
    }

    /**
     * Удаляет товар из корзины.
     *
     * @param productId идентификатор товара
     * @param redirectAttributes атрибуты для flash-сообщения
     * @return перенаправление на страницу корзины
     */
    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId,
                                 RedirectAttributes redirectAttributes) {
        cartService.removeItem(productId);
        redirectAttributes.addFlashAttribute("success", "Товар удалён из корзины");
        return "redirect:/cart";
    }

    /**
     * Обновляет количество товара в корзине.
     *
     * Если новое количество меньше или равно нулю, товар удаляется из корзины.
     *
     * @param productId идентификатор товара
     * @param quantity новое количество товара
     * @param redirectAttributes атрибуты для flash-сообщения
     * @return перенаправление на страницу корзины
     */
    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long productId,
                                 @RequestParam Integer quantity,
                                 RedirectAttributes redirectAttributes) {
        if (quantity <= 0) {
            cartService.removeItem(productId);
            redirectAttributes.addFlashAttribute("success", "Товар удалён из корзины");
        } else {
            cartService.updateQuantity(productId, quantity);
            redirectAttributes.addFlashAttribute("success", "Количество обновлено");
        }

        return "redirect:/cart";
    }

    /**
     * Полностью очищает корзину.
     *
     * @param redirectAttributes атрибуты для flash-сообщения
     * @return перенаправление на страницу корзины
     */
    @PostMapping("/clear")
    public String clearCart(RedirectAttributes redirectAttributes) {
        cartService.clearCart();
        redirectAttributes.addFlashAttribute("success", "Корзина очищена");
        return "redirect:/cart";
    }
}