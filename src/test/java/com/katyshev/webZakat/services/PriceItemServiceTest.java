package com.katyshev.webZakat.services;

import com.katyshev.webZakat.TestConfig;
import com.katyshev.webZakat.models.PriceItem;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class PriceItemServiceTest extends TestConfig {

    private final PriceItemService priceItemService;

    @Test
    void findAll() {
        List<PriceItem> priceItems = priceItemService.findAll();

        assertTrue(priceItems.size() > 0);
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 8, 15, 16, 23, 42})
    void getById(int id) {
        PriceItem byId = priceItemService.getById(id);

        assertEquals(id, byId.getId());
    }

    @Test
    void findAllPerPage() {
        List<PriceItem> priceItems = priceItemService.findAllPerPage("3", "30");

        assertEquals(30, priceItems.size());
        assertEquals(91, priceItems.get(0).getId());
    }

    @Test
    void findAllPerPageWithBadParameters() {
        List<PriceItem> priceItems = priceItemService.findAllPerPage("3qwe", "hello");

        assertEquals(30, priceItems.size());
        assertEquals(1, priceItems.get(0).getId());
    }

    @Test
    void importAllPrices() {
        priceItemService.importAllPrices();
        List<PriceItem> priceItems = priceItemService.findAll();

        assertTrue(priceItems.size() > 0);
    }

    @Test
    void setInOrder() {
        int id = 4815;
        int count = 23;
        priceItemService.setInOrder(id, count);
        PriceItem priceItem = priceItemService.getById(id);

        assertEquals(count, priceItem.getInOrder());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "0", "1", "3", "3.1", "0.9", "555", "5555555555", "H", "HELLO"})
    void validateCount(String count) {
        int id = 4815;
        int result = priceItemService.validateCount(id, count);
        int quantity = priceItemService.getById(id).getQuantity();

        assertTrue(result <= quantity);
    }
}