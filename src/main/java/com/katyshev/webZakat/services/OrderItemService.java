package com.katyshev.webZakat.services;

import com.katyshev.webZakat.models.OrderItem;
import com.katyshev.webZakat.models.PriceItem;
import com.katyshev.webZakat.repositories.OrderItemRepository;
import com.katyshev.webZakat.repositories.PriceItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final PriceItemRepository priceItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository, PriceItemRepository priceItemRepository) {
        this.orderItemRepository = orderItemRepository;
        this.priceItemRepository = priceItemRepository;
    }

    @Transactional
    public void save(int price_id, int quantity) {
        PriceItem priceItem = priceItemRepository.findById(price_id).get();
        OrderItem orderItem = new OrderItem();

        orderItem.setName(priceItem.getName());
        orderItem.setManufacturer(priceItem.getManufacturer());
        orderItem.setPrice(priceItem.getPrice());
        orderItem.setQuantity(quantity);
        orderItem.setDistributor(priceItem.getDist());

        orderItemRepository.save(orderItem);
    }
}
