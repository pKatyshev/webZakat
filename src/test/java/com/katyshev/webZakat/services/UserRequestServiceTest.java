package com.katyshev.webZakat.services;

import com.katyshev.webZakat.TestConfig;
import com.katyshev.webZakat.models.UserRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@RequiredArgsConstructor
class UserRequestServiceTest extends TestConfig {
    private final UserRequestService userRequestService;
    private final UserRequest forUpdate = new UserRequest(1, "updated request");
    private final UserRequest forSave = new UserRequest(33, "new request");

    @Test
    void findAll() {
        List<UserRequest> list = userRequestService.findAll();

        Assertions.assertNotNull(list);
        Assertions.assertTrue(list.size() > 0);

    }

    @Test
    void get() {
        UserRequest userRequest1 = userRequestService.get(1);
        UserRequest userRequest2 = userRequestService.get(3033);

        Assertions.assertNotNull(userRequest1);
        Assertions.assertNotNull(userRequest2);
        Assertions.assertEquals("Test1", userRequest1.getUserRequest());
        Assertions.assertNull(userRequest2.getUserRequest());
    }

    @Test
    void save() {
        UserRequest empty = userRequestService.get(33);
        Assertions.assertNull(empty.getUserRequest());

        userRequestService.save(forSave);

        UserRequest saved = userRequestService.get(33);

        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getUserRequest());
        Assertions.assertEquals(forSave, saved);
    }

    @Test
    void update() {
        userRequestService.save(forUpdate);

        UserRequest updated = userRequestService.get(1);

        Assertions.assertNotNull(updated);
        Assertions.assertNotNull(updated.getUserRequest());
        Assertions.assertEquals(forUpdate, updated);

    }

    @Test
    void saveOrUpdate() {
        userRequestService.saveOrUpdate(forUpdate);
        userRequestService.saveOrUpdate(forSave);

        String updated = userRequestService.get(1).getUserRequest();
        String saved = userRequestService.get(33).getUserRequest();

        Assertions.assertNotEquals("Test1", updated);
        Assertions.assertNotNull(saved);
        Assertions.assertEquals("updated request", updated);
        Assertions.assertEquals("new request", saved);
    }

}