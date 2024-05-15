package com.katyshev.webZakat.services;

import com.katyshev.webZakat.models.ArchiveItem;
import com.katyshev.webZakat.models.OrderItem;
import com.katyshev.webZakat.repositories.ArchiveItemRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Log
public class ArchiveItemService {
    private final ArchiveItemRepository archiveItemRepository;

    @Autowired
    public ArchiveItemService(ArchiveItemRepository archiveItemRepository) {
        this.archiveItemRepository = archiveItemRepository;
    }

    @Transactional
    public void save(List<ArchiveItem> items) {
        archiveItemRepository.saveAll(items);
    }

    @Transactional
    public void saveOrder(List<OrderItem> order) {
        int lastOrderNumber = archiveItemRepository.getLastOrderNumber().orElse(0);
        int orderNumber = lastOrderNumber + 1;
        List<ArchiveItem> archiveItems = convertOrderToArchiveList(order, orderNumber);
        save(archiveItems);
    }

    public List<ArchiveItem> findAll() {
        return archiveItemRepository.findAll();
    }

    public List<ArchiveItem> convertOrderToArchiveList(List<OrderItem> order, int orderNumber) {
        ArrayList<ArchiveItem> archiveItems = new ArrayList<>();

        for(OrderItem orderItem : order) {
            ArchiveItem archiveItem = new ArchiveItem();
            archiveItem.setOrderNumber(orderNumber);
            archiveItem.setPriceName(orderItem.getPriceItem().getName());
            archiveItem.setPrice(orderItem.getPriceItem().getPrice());
            archiveItem.setQuantity(orderItem.getInOrder());
            archiveItem.setExportTime(orderItem.getExportTime());

            archiveItems.add(archiveItem);
        }
        return archiveItems;
    }
}
