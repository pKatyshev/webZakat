package com.katyshev.webZakat.services;

import com.katyshev.webZakat.models.PriceItem;
import com.katyshev.webZakat.repositories.PriceItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PriceItemService {

    private final PriceItemRepository priceItemRepository;

    @Autowired
    public PriceItemService(PriceItemRepository priceItemRepository) {
        this.priceItemRepository = priceItemRepository;
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
            return priceItemRepository.findAll(PageRequest.of(1, 30)).getContent();
        }

        return priceItemRepository.findAll(PageRequest.of(pageNumber, countPerPage)).getContent();
    }

    @Transactional
    public void saveAll(List<PriceItem> list) {
        priceItemRepository.saveAll(list);
    }

    @Transactional
    public void saveAllAsNew(List<PriceItem> list) {
        priceItemRepository.truncateTable();
        priceItemRepository.restartSequence();
        priceItemRepository.saveAll(list);
    }

    @Transactional
    public void setInOrder(int id, int count) {
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
}
