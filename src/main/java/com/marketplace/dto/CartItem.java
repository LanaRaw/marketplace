package com.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double price;

    // Метод для подсчета суммы за позицию
    public Double getTotal() {
        return price * quantity;
    }
}