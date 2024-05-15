package com.katyshev.webZakat.services;

import com.katyshev.webZakat.TestConfig;
import com.katyshev.webZakat.models.OrderItem;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RequiredArgsConstructor
class ArchiveItemServiceTest extends TestConfig {

    @Autowired
    private final ArchiveItemService archiveItemService;
    @Autowired
    private final OrderItemService orderItemService;

    @Test
    void saveOrder() {
        int startSize = archiveItemService.findAll().size();
        List<OrderItem> orderItems = orderItemService.findAll();
        archiveItemService.saveOrder(orderItems);
        int stopSize = archiveItemService.findAll().size();

        Assertions.assertNotEquals(startSize, stopSize);
        Assertions.assertEquals(stopSize-startSize, orderItems.size());
    }
}