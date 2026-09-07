package com.marketplace.controller;

import com.marketplace.model.Category;
import com.marketplace.model.Product;
import com.marketplace.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

/**
 * Обрабатывает запросы, связанные с просмотром товаров.
 */
@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Отображает список товаров с поддержкой пагинации и фильтрации.
     *
     * @param page номер текущей страницы
     * @param size количество товаров на странице
     * @param search поисковый запрос
     * @param category категория для фильтрации
     * @param model модель для передачи данных в представление
     * @return имя шаблона списка товаров
     */
    @GetMapping
    public String listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Category category,
            Model model) {

        Page<Product> productPage = productService.searchAndFilter(search, category, page, size);
        List<Category> categories = productService.getAllCategories();

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("categories", categories);
        model.addAttribute("size", size);

        return "product/list";
    }

    /**
     * Отображает детальную информацию о товаре.
     *
     * Если товар не найден или неактивен, выполняется перенаправление
     * на страницу со списком товаров.
     *
     * @param id идентификатор товара
     * @param model модель для передачи товара в представление
     * @return имя шаблона страницы товара или перенаправление на список товаров
     */
    @GetMapping("/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Optional<Product> productOpt = productService.getProductById(id);

        if (productOpt.isEmpty() || !productOpt.get().getIsActive()) {
            return "redirect:/products";
        }

        model.addAttribute("product", productOpt.get());
        return "product/detail";
    }
}