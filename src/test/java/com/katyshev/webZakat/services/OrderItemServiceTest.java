package com.katyshev.webZakat.services;

import com.katyshev.webZakat.TestConfig;
import com.katyshev.webZakat.models.OrderItem;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@RequiredArgsConstructor
class OrderItemServiceTest extends TestConfig {

    @Autowired
    private final OrderItemService orderItemService;
    @Autowired
    private final ArchiveItemService archiveItemService;

    @Test
    void save() {
        int id = 8;
        int count = 1;

        orderItemService.save(id, count);
        OrderItem byId = orderItemService.findByPriceItemId(id);

        Assertions.assertEquals(id, byId.getPriceItem().getId());
        Assertions.assertEquals(count, byId.getInOrder());
    }

    @Test
    void findAll() {
        List<OrderItem> orderItems = orderItemService.findAll();

        Assertions.assertTrue(orderItems.size() > 0);
    }

    @Test
    @Sql(statements = "TRUNCATE TABLE order_item")
    void findAllEmptyTable() {
        List<OrderItem> orderItems = orderItemService.findAll();

        Assertions.assertEquals(0, orderItems.size());
    }

    @Test
    void delete() {
        int priceItemId = orderItemService.findAll().get(0).getPriceItem().getId();
        orderItemService.delete(priceItemId);
        OrderItem byId = orderItemService.findById(priceItemId);

        Assertions.assertNull(byId);
    }

    @Test
    void edit() {
        int priceItemId = orderItemService.findAll().get(0).getPriceItem().getId();
        int count = 20;
        orderItemService.edit(priceItemId, count);
        int inOrder = orderItemService.findByPriceItemId(priceItemId).getInOrder();

        Assertions.assertEquals(count,inOrder);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -5})
    void edit(int count) {
        int priceItemId = orderItemService.findAll().get(0).getPriceItem().getId();
        orderItemService.edit(priceItemId, count);
        OrderItem byId = orderItemService.findByPriceItemId(priceItemId);

        Assertions.assertNull(byId);
    }

    @Test
    void export() {
        int totalInOrder = orderItemService.findAll().size();
        int totalInArchive = archiveItemService.findAll().size();
        orderItemService.export();
        int afterInOrder = orderItemService.findAll().size();
        int afterInArchive = archiveItemService.findAll().size();

        Assertions.assertNotEquals(totalInOrder, afterInOrder);
        Assertions.assertNotEquals(totalInArchive, afterInArchive);
        Assertions.assertEquals(afterInOrder, 0);
        Assertions.assertEquals(totalInOrder, afterInArchive - totalInArchive);

    }
}