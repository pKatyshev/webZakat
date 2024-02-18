package com.katyshev.webZakat.repositories;

import com.katyshev.webZakat.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {


    @Transactional
    void deleteByPriceItemId(int priceItemId);

    @Transactional
    OrderItem findById(int Id);

    OrderItem findByPriceItemId(int id);
}
