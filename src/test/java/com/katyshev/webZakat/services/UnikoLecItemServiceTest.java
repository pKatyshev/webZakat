package com.katyshev.webZakat.services;

import com.katyshev.webZakat.models.UnikoLecItem;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestPropertySource("/test.properties")
@Transactional

@RequiredArgsConstructor
class UnikoLecItemServiceTest {

    @Autowired
    private final UnikoLecItemService unikoLecItemService;


    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    void findById(int id) {
        UnikoLecItem item = unikoLecItemService.findById(id);
        Assertions.assertEquals(item.getId(), id);
    }
}