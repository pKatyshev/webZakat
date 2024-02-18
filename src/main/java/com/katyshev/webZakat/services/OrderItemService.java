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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public void save(int priceItemId, int quantity) {
        log.info(String.format("Save in OrderItem, priceItem=%s, quantity=%s", priceItemId, quantity));

        if (quantity <= 0) {
            delete(priceItemId);
            return;
        }

        PriceItem priceItem = priceItemRepository.findById(priceItemId).get();
        List<OrderItem> alreadyInOrderList = orderItemRepository.findAll();;

        for (OrderItem already : alreadyInOrderList) {
            if (already.getPriceItem().getId() == priceItemId) {
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

    public List<OrderItem> findAll() {
        return orderItemRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(OrderItem::getId))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(int priceItemId) {
        orderItemRepository.deleteByPriceItemId(priceItemId);
    }

    @Transactional
    public void edit(int priceItemId, int count) {
        log.info(String.format("Editing priceItemId = %s, count = %s)", priceItemId, count));

        if (count <= 0) {
            log.warning(String.format("Deleting priceItemId = %s", priceItemId));
            delete(priceItemId);
            return;
        }

        OrderItem orderItem = orderItemRepository.findByPriceItemId(priceItemId);

        orderItem.setInOrder(count);
        orderItem.getPriceItem().setInOrder(count);
    }
}
