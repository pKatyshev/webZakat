package com.katyshev.webZakat.utils;

import com.katyshev.webZakat.models.PriceItem;

import java.util.List;

public interface Exporter {
    void exportOrder(List<PriceItem> order);
}
