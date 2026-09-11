package com.marketplace.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Содержит данные, которые пользователь вводит при оформлении заказа.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {

    @NotBlank(message = "Адрес доставки обязателен")
    private String deliveryAddress;

    @NotBlank(message = "Телефон обязателен")
    private String phoneNumber;

    private String comment;
}