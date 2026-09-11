package com.marketplace.config;

import com.marketplace.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Глобальный контроллер-советник.
 *
 * Автоматически загружает корзину пользователя из БД (если она пуста в сессии)
 * перед обработкой каждого запроса. Это гарантирует, что бейдж корзины
 * и страница корзины всегда отображают актуальные данные.
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final CartService cartService;

    /**
     * Добавляет {@link CartService} в модель для всех контроллеров.
     * Если корзина в сессии пуста, пытается загрузить её из БД.
     *
     * @return сервис корзины
     */
    @ModelAttribute("cartService")
    public CartService getCartService() {
        if (cartService.getItems().isEmpty()) {
            cartService.loadFromDatabase();
        }
        return cartService;
    }
}