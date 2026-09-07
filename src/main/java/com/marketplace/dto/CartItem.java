package com.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Содержит информацию о товаре в корзине.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private Long productId;
    private String productName;
    private Integer quantity;
    private Double price;

    /**
     * Рассчитывает общую стоимость позиции в корзине.
     *
     * @return общая стоимость товара с учетом количества
     */
    public Double getTotal() {
        return price * quantity;
    }
}