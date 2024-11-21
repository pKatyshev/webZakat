package com.katyshev.webZakat.utils;

import com.katyshev.webZakat.models.PriceItem;
import com.katyshev.webZakat.models.UnikoLecItem;

import java.util.List;

public interface Importer {

    List<UnikoLecItem> importUnikoQuery();

    List<PriceItem> importAllPrices();
}
