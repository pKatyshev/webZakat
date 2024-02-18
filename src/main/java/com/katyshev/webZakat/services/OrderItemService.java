package com.katyshev.webZakat.services;

import com.katyshev.webZakat.models.OrderItem;
import com.katyshev.webZakat.models.PriceItem;
import com.katyshev.webZakat.repositories.OrderItemRepository;
import com.katyshev.webZakat.repositories.PriceItemRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Log
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final PriceItemRepository priceItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository, PriceItemRepository priceItemRepository) {
        this.orderItemRepository = orderItemRepository;
        this.priceItemRepository = priceItemRepository;
    }

    @Transactional
    public void save(int price_item_id, int quantity) {
        log.warning(String.format("Save in OrderItem, priceItem=%s, quantity=%s", price_item_id, quantity));

        if (quantity <= 0) {
            delete(price_item_id);
            return;
        }

        PriceItem priceItem = priceItemRepository.findById(price_item_id).get();
        List<OrderItem> alreadyInOrderList = orderItemRepository.findAll();;

        for (OrderItem already : alreadyInOrderList) {
            if (already.getPriceItem().getId() == price_item_id) {
                if (already.getInOrder() != quantity) {
                    orderItemRepository.findById(already.getId()).setInOrder(quantity);
                }
                return;
            }
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setInOrder(quantity);
        orderItem.setPriceItem(priceItem);
        orderItem.setCreateTime(LocalDateTime.now());

        orderItemRepository.save(orderItem);
    }

    public void delete(int priceItemId) {
        orderItemRepository.deleteByPriceItemId(priceItemId);
    }
}
