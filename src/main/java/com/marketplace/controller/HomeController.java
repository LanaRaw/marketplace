package com.marketplace.controller;

import com.marketplace.model.Product;
import com.marketplace.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Обрабатывает запросы главной страницы приложения.
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductRepository productRepository;

    /**
     * Отображает главную страницу и загружает последние активные товары.
     *
     * @param model модель для передачи товаров в представление
     * @return имя шаблона главной страницы
     */
    @GetMapping("/")
    public String home(Model model) {
        List<Product> latestProducts = productRepository.findByIsActiveTrue(
                PageRequest.of(0, 6, Sort.by("createdAt").descending())
        ).getContent();

        model.addAttribute("latestProducts", latestProducts);
        return "home";
    }
}