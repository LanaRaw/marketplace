package com.marketplace.model;

/**
 * Определяет статусы заказа.
 */
public enum OrderStatus {
    CREATED,
    PAID,
    SHIPPED,
    DELIVERED,
    CANCELLED
}