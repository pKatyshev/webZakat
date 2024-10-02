package com.katyshev.webZakat.services;

import com.katyshev.webZakat.dto.UiDTO;
import com.katyshev.webZakat.exceptions.UnikoItemListIsEmptyException;
import com.katyshev.webZakat.models.PriceItem;
import com.katyshev.webZakat.models.UnikoLecItem;
import com.katyshev.webZakat.models.UserRequest;
import com.katyshev.webZakat.repositories.UnikoLecItemRepository;
import com.katyshev.webZakat.utils.Engine;
import com.katyshev.webZakat.utils.Importer;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log
@Service
@Transactional(readOnly = true)
public class UnikoLecItemService {

    private final UnikoLecItemRepository unikoLecItemRepository;
    private final UserRequestService userRequestService;
    private final Importer importer;
    private final Engine engine;

    @Autowired
    public UnikoLecItemService(UnikoLecItemRepository unikoLecItemRepository, UserRequestService userRequestService, Importer importer, Engine engine) {
        this.unikoLecItemRepository = unikoLecItemRepository;
        this.userRequestService = userRequestService;
        this.importer = importer;
        this.engine = engine;
    }

    public List<UnikoLecItem> findAll() {
        return unikoLecItemRepository.findAll();
    }

    @Transactional
    public void save(UnikoLecItem unikoLecItem) {
        unikoLecItemRepository.save(unikoLecItem);
    }

    @Transactional
    public void importNewUnikoQuery() {
        List<UnikoLecItem> newQuery = importer.importUnikoQuery();
        unikoLecItemRepository.truncateTable();
        unikoLecItemRepository.restartSequence();
        unikoLecItemRepository.saveAll(newQuery);
        log.info("uniko query was import successfully");
    }

    @Transactional
    public void deleteAll() {
        unikoLecItemRepository.deleteAll();
    }

    @Transactional
    public void deleteById(int id) {
        unikoLecItemRepository.deleteById(id);
    }

    public UnikoLecItem findById(int id) {
        Optional<UnikoLecItem> unikoItem = unikoLecItemRepository.findById(id);
        return unikoItem.orElseThrow();
    }

    public UnikoLecItem findNextById(int id) {
        Optional<Integer> optional = unikoLecItemRepository.getMaxId();
        int maxId = optional.orElseThrow(UnikoItemListIsEmptyException::new);
        if (id > maxId) id = 1;

        Optional<UnikoLecItem> unikoItem = unikoLecItemRepository.findById(id);

        while (unikoItem.isEmpty()) {
            unikoItem = unikoLecItemRepository.findById(++id);
        }

        return unikoItem.get();
    }

    @Transactional
    public UiDTO nextRequest(int positionNumber, String user_request) {
        UiDTO uiDTO = new UiDTO();
        List<PriceItem> list;
        UnikoLecItem unikoItem;

        try {
            unikoItem = findNextById(positionNumber);
        } catch (UnikoItemListIsEmptyException e) {
            log.warning("UnikoItemList is empty!");
            uiDTO.setPosition(positionNumber);
            uiDTO.setName("");
            uiDTO.setPriceItemList(new ArrayList<>());
            uiDTO.setRequest(user_request);
            return uiDTO;
        }

        String request = "";

        if (Objects.isNull(user_request)) {             // - auto request
            String savedUserRequest = userRequestService.get(unikoItem.getOrderGroup()).getUserRequest();

            if (savedUserRequest != null) {
                request = savedUserRequest;
            } else {
                request = engine.getProgramRequest(unikoItem.getName());
            }

            list = engine.getPriceListForStringCustom(request);

        } else {                                        // - manual request
            if (StringUtils.isBlank(user_request)) {    // - if manual request is empty
                list = new ArrayList<>();
            } else {                                    // - correct manual request
                list = engine.getPriceListForStringCustom(user_request);
                if (list.size() > 0) {
                    userRequestService.saveOrUpdate(new UserRequest(unikoItem.getOrderGroup(), user_request));
                }
                request = user_request;
            }
        }

        uiDTO.setPosition(unikoItem.getId());
        uiDTO.setName(unikoItem.getName());
        uiDTO.setPriceItemList(list);
        uiDTO.setRequest(request);

        return uiDTO;
    }
}
