package com.katyshev.webZakat.services;

import com.katyshev.webZakat.models.UnikoLecItem;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestPropertySource("/test.properties")
@Transactional

@RequiredArgsConstructor
class UnikoLecItemServiceTest {

    @Autowired
    private final UnikoLecItemService unikoLecItemService;

}