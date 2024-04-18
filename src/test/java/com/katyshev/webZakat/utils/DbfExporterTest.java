package com.katyshev.webZakat.utils;

import com.katyshev.webZakat.services.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestPropertySource("/test.properties")
@RequiredArgsConstructor
class DbfExporterTest {

    @Autowired
    private final OrderItemService orderItemService;

    @Test
    void exportOrder() {
        orderItemService.export();

    }
}