package com.katyshev.webZakat.unit;

import com.katyshev.webZakat.utils.MyFileManager;
import org.junit.jupiter.api.Test;

class MyFileManagerTest {

    @Test
    void moveUnikoQueryToStorage() {
        MyFileManager myFileManager = new MyFileManager();
        myFileManager.moveToStorage("M:\\ZakatIO\\query\\OrdByKred_22-06-22_16-34.dbf");
    }
}