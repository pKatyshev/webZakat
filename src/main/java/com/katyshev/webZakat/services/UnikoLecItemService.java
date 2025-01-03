package com.katyshev.webZakat.services;

import com.katyshev.webZakat.dto.UiDTO;
import com.katyshev.webZakat.exceptions.UnikoItemListIsEmptyException;
import com.katyshev.webZakat.models.PriceItem;
import com.katyshev.webZakat.models.UnikoLecItem;
import com.katyshev.webZakat.models.UserRequest;
import com.katyshev.webZakat.repositories.PriceItemRepository;
import com.katyshev.webZakat.repositories.UnikoLecItemRepository;
import com.katyshev.webZakat.utils.FileManager;
import com.katyshev.webZakat.utils.Importer;
import com.katyshev.webZakat.utils.WordsWorker;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Log
@Service
@Transactional(readOnly = true)
public class UnikoLecItemService {

    private final UnikoLecItemRepository unikoLecItemRepository;
    private final PriceItemRepository priceItemRepository;
    private final PriceItemService priceItemService;
    private final UserRequestService userRequestService;
    private final WordsWorker wordsWorker;
    private final FileManager fileManager;
    private final Importer importer;

    @Autowired
    public UnikoLecItemService(UnikoLecItemRepository unikoLecItemRepository,
                               PriceItemRepository priceItemRepository,
                               PriceItemService priceItemService, UserRequestService userRequestService,
                               WordsWorker wordsWorker, FileManager fileManager, Importer importer) {
        this.unikoLecItemRepository = unikoLecItemRepository;
        this.priceItemRepository = priceItemRepository;
        this.priceItemService = priceItemService;
        this.userRequestService = userRequestService;
        this.wordsWorker = wordsWorker;
        this.fileManager = fileManager;
        this.importer = importer;
    }

    public List<UnikoLecItem> findAll() {
        List<UnikoLecItem> lecItems = unikoLecItemRepository.findAll();
        Collections.sort(lecItems);
        return lecItems;
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
        fileManager.moveUnikoQueryToStorage();
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

    @Transactional
    public UiDTO nextRequest(int positionNumber, String user_request, boolean next) {
        UiDTO uiDTO = new UiDTO();
        List<PriceItem> list;
        UnikoLecItem unikoItem;

        try {
            unikoItem = findNextById(positionNumber, next);
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
                request = wordsWorker.generateSearchQuery(unikoItem.getName());
            }

            list = priceItemService.getPriceListForString(request);

        } else {                                        // - manual request
            if (StringUtils.isBlank(user_request)) {    // - if manual request is empty
                list = new ArrayList<>();
            } else {                                    // - correct manual request
                list = priceItemService.getPriceListForString(user_request);
                if (list.size() > 0) {
                    userRequestService.saveOrUpdate(new UserRequest(unikoItem.getOrderGroup(), user_request));
                }
                request = user_request;
            }
        }

        uiDTO.setRequestCount(unikoItem.getQuantity());
        uiDTO.setPosition(unikoItem.getId());
        uiDTO.setName(unikoItem.getName());
        uiDTO.setPriceItemList(list);
        uiDTO.setRequest(request);
        uiDTO.setTotalAmount(priceItemRepository.getTotalAmount().orElse(new BigDecimal("0.00")));

        return uiDTO;
    }

    public UnikoLecItem findNextById(int id, boolean next) {
        Optional<Integer> optional = unikoLecItemRepository.getMaxId();
        int maxId = optional.orElseThrow(UnikoItemListIsEmptyException::new);
        if (id > maxId) id = 1;
        if (id <= 0 ) id = maxId;

        return getUnikoLecItem(id, next);
    }

    public UnikoLecItem findNextById(int id) {
        return findNextById(id, true);
    }

    private UnikoLecItem getUnikoLecItem(int id, boolean next) {

        Optional<UnikoLecItem> unikoItem = unikoLecItemRepository.findById(id);

        while (unikoItem.isEmpty()) {
            if (next) {
                unikoItem = unikoLecItemRepository.findById(++id);
            } else {
                unikoItem = unikoLecItemRepository.findById(--id);
            }
        }
        return unikoItem.get();
    }

    public UnikoLecItem findById(int id) {
        Optional<UnikoLecItem> unikoItem = unikoLecItemRepository.findById(id);
        return unikoItem.orElseThrow();
    }

    @Transactional
    public void setOrdered(int position, int count) {
        UnikoLecItem item = findById(position);
        item.setOrdered(count);
    }
}
