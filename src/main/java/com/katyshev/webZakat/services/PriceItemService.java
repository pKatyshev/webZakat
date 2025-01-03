package com.katyshev.webZakat.services;

import com.katyshev.webZakat.dto.UiDTO;
import com.katyshev.webZakat.models.PriceItem;
import com.katyshev.webZakat.repositories.CustomPriceItemRepository;
import com.katyshev.webZakat.repositories.PriceItemRepository;
import com.katyshev.webZakat.utils.DBFImporter;
import com.katyshev.webZakat.utils.WordsWorker;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log
@Service
@Transactional(readOnly = true)
public class PriceItemService {

    private final PriceItemRepository priceItemRepository;
    private final CustomPriceItemRepository customPriceItemRepository;
    private final WordsWorker wordsWorker;
    private final DBFImporter importer;

    @Autowired
    public PriceItemService(PriceItemRepository priceItemRepository,
                            CustomPriceItemRepository customPriceItemRepository,
                            WordsWorker wordsWorker, DBFImporter importer) {

        this.priceItemRepository = priceItemRepository;
        this.customPriceItemRepository = customPriceItemRepository;
        this.wordsWorker = wordsWorker;
        this.importer = importer;
    }

    public List<PriceItem> findAll() {
        return priceItemRepository.findAll(Sort.by("price"));
    }

    public List<PriceItem> findAll(int page, int itemsPerPage) {
        return priceItemRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent();
    }

    public PriceItem getById (int id) {
        Optional<PriceItem> priceItemOptional = priceItemRepository.findById(id);
        return priceItemOptional.orElseGet(PriceItem::new);
    }

    public List<PriceItem> findAllPerPage(String page, String itemsPerPage) {
        int pageNumber;
        int countPerPage;

        try {
            pageNumber = Integer.parseInt(page);
            countPerPage = Integer.parseInt(itemsPerPage);
        } catch (NumberFormatException e) {
            return priceItemRepository.findAll(PageRequest.of(0, 30)).getContent();
        }

        return priceItemRepository.findAll(PageRequest.of(pageNumber, countPerPage)).getContent();
    }

    @Transactional
    public void saveAll(List<PriceItem> list) {
        priceItemRepository.saveAll(list);
    }

    @Transactional
    public void importAllPrices() {
        List<PriceItem> newPrices = importer.importAllPrices();
        priceItemRepository.truncateTable();
        priceItemRepository.restartSequence();
        priceItemRepository.saveAll(newPrices);
    }

    @Transactional
    public void setInOrder(int id, int count) {
        log.info(String.format("Save in PriceItem, priceItem=%s, quantity=%s", id, count));
        PriceItem priceItem = priceItemRepository.findById(id).get();
        priceItem.setInOrder(count);
    }

    public int validateCount(int priceItemId, String countString) {
        PriceItem priceItem = getById(priceItemId);
        int balance = priceItem.getQuantity();
        int count;

        try {
            count = Integer.parseInt(countString);
        } catch (NumberFormatException e) {
            return 0;
        }

        if (balance < count) {
            return balance;
        } else return Math.max(count, 0);
    }

    public UiDTO userRequest(String request) {
        UiDTO uiDTO = new UiDTO();
        List<PriceItem> list;

        if (StringUtils.isBlank(request)) {
            list = new ArrayList<>(findAllPerPage("1", "30"));
        } else {
            list = getPriceListForString(request);
        }

        uiDTO.setRequest(request);
        uiDTO.setPriceItemList(list);

        return uiDTO;
    }

    public List<PriceItem> getPriceListForString(String request) {
        String formatted = wordsWorker.formatRequest(request);
        return sortByPriceAndId(findAllByQuery(formatted));
    }

    private List<PriceItem> sortByPriceAndId(List<PriceItem> list) {
        return list.stream()
                .sorted(
                        Comparator.comparing(PriceItem::getPrice).thenComparing(PriceItem::getCodePst)
                ).collect(Collectors.toList());
    }


    public List<PriceItem> findAllByQuery(String query) {
        return customPriceItemRepository.findAllByQuery(query);
    }
}
