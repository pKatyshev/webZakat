package com.katyshev.webZakat.services;

import com.katyshev.webZakat.models.OrderItem;
import com.katyshev.webZakat.models.PriceItem;
import com.katyshev.webZakat.repositories.OrderItemRepository;
import com.katyshev.webZakat.repositories.PriceItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public void save(int price_item_id, int quantity) {

        if (quantity <= 0) {
            delete(price_item_id);
            return;
        }

        PriceItem priceItem = priceItemRepository.findById(price_item_id).get();
        List<OrderItem> alreadyInOrderList = orderItemRepository.findAll();
        OrderItem orderItem = new OrderItem();

        orderItem.setName(priceItem.getName());
        orderItem.setManufacturer(priceItem.getManufacturer());
        orderItem.setPrice(priceItem.getPrice());
        orderItem.setQuantity(quantity);
        orderItem.setDistributor(priceItem.getDist());
        orderItem.setPriceItemId(price_item_id);

        for (OrderItem already : alreadyInOrderList) {
            if (already.getPriceItemId() == orderItem.getPriceItemId()) {
                if (already.getQuantity() != quantity) {
                    orderItemRepository.findById(already.getId()).setQuantity(quantity);
                }
                return;
            }
        }

        orderItemRepository.save(orderItem);
    }

    public void delete(int priceItemId) {
        orderItemRepository.deleteByPriceItemId(priceItemId);
    }
}
