package com.katyshev.webZakat.repositories;

import com.katyshev.webZakat.models.PriceItem;

import java.util.List;

public interface CustomPriceItemRepository {
    List<PriceItem> findAllByQuery(String query);
}
