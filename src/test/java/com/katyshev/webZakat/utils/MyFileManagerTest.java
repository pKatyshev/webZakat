package com.katyshev.webZakat.utils;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestPropertySource("/test.properties")
@RequiredArgsConstructor
class MyFileManagerTest {

    @Autowired
    private final MyFileManager fileManager;

    @Test
    void createOutputFile() {
        fileManager.getNewOutputFile();
    }
}