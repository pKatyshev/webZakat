package com.katyshev.webZakat.utils;

import com.katyshev.webZakat.models.ArchiveItem;
import com.katyshev.webZakat.models.PriceItem;
import com.katyshev.webZakat.services.PriceItemService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Log
public class Engine {
    private final PriceItemService priceItemService;
    private final WordsWorker wordsWorker;

    @Autowired
    public Engine(PriceItemService priceItemService,
                  WordsWorker wordsWorker) {
        this.priceItemService = priceItemService;
        this.wordsWorker = wordsWorker;
    }

    public String getProgramRequest(String name) {
        return wordsWorker.generateSearchQuery(name);
    }

    public List<PriceItem> getPriceListForStringCustom(String request) {
        String formatted = wordsWorker.formatRequest(request);
        return sortByPriceAndId(priceItemService.findAllByQuery(formatted));
    }

    private List<PriceItem> sortByPriceAndId(List<PriceItem> list) {
        return list.stream()
                .sorted(
                        Comparator.comparing(PriceItem::getPrice).thenComparing(PriceItem::getCodePst)
                ).collect(Collectors.toList());
    }
}
