package com.marketplace.repository;

import com.marketplace.model.Order;
import com.marketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Находит заказы покупателя, отсортированные по дате от новых к старым.
     *
     * @param buyer покупатель
     * @return список заказов покупателя
     */
    List<Order> findByBuyerOrderByOrderDateDesc(User buyer);
}