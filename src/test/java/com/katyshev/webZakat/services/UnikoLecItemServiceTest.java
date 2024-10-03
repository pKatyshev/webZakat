package com.katyshev.webZakat.services;

import com.katyshev.webZakat.TestConfig;
import com.katyshev.webZakat.dto.UiDTO;
import com.katyshev.webZakat.exceptions.UnikoItemListIsEmptyException;
import com.katyshev.webZakat.models.UnikoLecItem;
import com.katyshev.webZakat.models.UserRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class UnikoLecItemServiceTest extends TestConfig {

    private final UnikoLecItemService unikoLecItemService;
    private final UserRequestService userRequestService;

    @Test
    void findAll() {
        List<UnikoLecItem> unikoLecItems = unikoLecItemService.findAll();

        assertFalse(unikoLecItems.isEmpty());
    }

    @Test
    @Disabled
    void importNewUnikoQuery() {
        unikoLecItemService.importNewUnikoQuery();
        List<UnikoLecItem> unikoLecItems = unikoLecItemService.findAll();

        assertFalse(unikoLecItems.isEmpty());
    }

    @Test
    void deleteAll() {
        unikoLecItemService.deleteAll();
        List<UnikoLecItem> unikoLecItems = unikoLecItemService.findAll();

        assertTrue(unikoLecItems.isEmpty());
    }

    @Test()
    void deleteById() {
        int id = 2;
        unikoLecItemService.deleteById(id);

        assertThrows(NoSuchElementException.class, () -> unikoLecItemService.findById(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 10})
    void findNextById(int id) {
        UnikoLecItem item = unikoLecItemService.findNextById(id);

        assertNotNull(item);
    }

    @Test
    void findNextByIdEmptyException() {
        unikoLecItemService.deleteAll();
        assertThrows(UnikoItemListIsEmptyException.class, () -> unikoLecItemService.findNextById(1));
    }

    @Test
    void nextRequest() {
        UiDTO uiDTO = unikoLecItemService.nextRequest(3, null, true);

        assertNotNull(uiDTO.getRequest());
        assertNotNull(uiDTO.getName());
        assertNotNull(uiDTO.getPriceItemList());
    }

    @Test
    void nextRequestWitnEmptyList() {
        unikoLecItemService.deleteAll();
        UiDTO uiDTO = unikoLecItemService.nextRequest(3, null, true);

        assertNotNull(uiDTO.getName());
        assertNotNull(uiDTO.getPriceItemList());
    }

    @ParameterizedTest
    @ValueSource(strings = {"парацетамол", "ПАРАЦЕТАМОЛ", "Арбидол", "10 20"})
    void nextRequestWitnUserRequest(String request) {
        unikoLecItemService.deleteAll();
        UiDTO uiDTO = unikoLecItemService.nextRequest(3, request, true);

        assertNotNull(uiDTO.getRequest());
        assertNotNull(uiDTO.getName());
        assertNotNull(uiDTO.getPriceItemList());
    }

    @Test
    void nextRequsetWithUserSavedRequest() {
        UiDTO uiDTO = unikoLecItemService.nextRequest(10, null, true);
        assertNotEquals("TestRequest", uiDTO.getRequest());

        userRequestService.save(new UserRequest(5726, "TestRequest"));

        uiDTO = unikoLecItemService.nextRequest(10, null, true);
        assertEquals("TestRequest", uiDTO.getRequest());
    }

    @Test
    void nextRequsetWithSaveNewUserRequest() {
        UiDTO uiDTO = unikoLecItemService.nextRequest(11, "50", true);

        String savedUserRequest = userRequestService.get(5976).getUserRequest();

        assertEquals("50", savedUserRequest);
    }


}