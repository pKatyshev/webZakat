package com.katyshev.webZakat.services;

import com.katyshev.webZakat.exceptions.PositionIndexOfBound;
import com.katyshev.webZakat.exceptions.UnikoItemListIsEmptyException;
import com.katyshev.webZakat.models.UnikoLecItem;
import com.katyshev.webZakat.repositories.UnikoLecItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UnikoLecItemService {

    private final UnikoLecItemRepository unikoLecItemRepository;

    @Autowired
    public UnikoLecItemService(UnikoLecItemRepository unikoLecItemRepository) {
        this.unikoLecItemRepository = unikoLecItemRepository;
    }

    public List<UnikoLecItem> findAll() {
        return unikoLecItemRepository.findAll();
    }

    @Transactional
    public void save(UnikoLecItem unikoLecItem) {
        unikoLecItemRepository.save(unikoLecItem);
    }

    @Transactional
    public void saveAll(List<UnikoLecItem> list) {
        unikoLecItemRepository.saveAll(list);
    }

    @Transactional
    public void saveAllAsNew(List<UnikoLecItem> list) {
        unikoLecItemRepository.truncateTable();
        unikoLecItemRepository.restartSequence();
        unikoLecItemRepository.saveAll(list);
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
        if (unikoItem.isPresent()) {
            return unikoItem.get();
        } else {
            throw new PositionIndexOfBound();
        }
    }

    public UnikoLecItem findNextById(int id) {
        Optional<Integer> optional = unikoLecItemRepository.getMaxId();
        if (optional.isEmpty()) throw new UnikoItemListIsEmptyException();

        int maxId = optional.get();

        if (id > maxId) id = 1;

        Optional<UnikoLecItem> unikoItem = unikoLecItemRepository.findById(id);

        while (unikoItem.isEmpty()) {
            unikoItem = unikoLecItemRepository.findById(++id);
        }

        return unikoItem.get();
    }
}
