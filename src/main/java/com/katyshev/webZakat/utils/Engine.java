package com.katyshev.webZakat.utils;

import com.katyshev.webZakat.models.PriceItem;
import com.katyshev.webZakat.models.UnikoLecItem;
import com.katyshev.webZakat.services.PriceItemService;
import com.katyshev.webZakat.services.UnikoLecItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class Engine {
    private final Importer importer;
    private final UnikoLecItemService unikoLecItemService;
    private final PriceItemService priceItemService;
    private final WordsWorker wordsWorker;

    @Autowired
    public Engine(Importer importer, UnikoLecItemService unikoLecItemService, PriceItemService priceItemService, WordsWorker wordsWorker) {
        this.importer = importer;
        this.unikoLecItemService = unikoLecItemService;
        this.priceItemService = priceItemService;
        this.wordsWorker = wordsWorker;
    }


    public void importNewUnikoQuery() {
        unikoLecItemService.saveAllAsNew(importer.importUnikoQuery());
        System.out.println("Engine: QUERY imported successfully");
    }

    public void importAllPrices() {
        priceItemService.saveAllAsNew(importer.importAllPrices());
        System.out.println("Engine: PRICES imported successfully");
    }

    public List<PriceItem> getPriceListForUnikoItem(UnikoLecItem unikoItem) {
        String searchQuery = wordsWorker.generateSearchQuery(unikoItem.getName());
        List<PriceItem> response = getPriceListForString(searchQuery);
        return response;
    }

    public List<PriceItem> getPriceListForString(String searchQuery) {
        String[] arr = searchQuery.split(" ");
        List<PriceItem> priceItems = priceItemService.findAll();
        List<PriceItem> response = new ArrayList<>();

        System.out.println("search for: " + searchQuery);

        long start = new Date().getTime();

        for (PriceItem priceItem : priceItems) {
            boolean found = false;
            for (int i = 0; i < arr.length; i++) {
                if (StringUtils.containsIgnoreCase(priceItem.getName(), arr[i])) {
                    found = true;
                } else {
                    found = false;
                    break;
                }
            }
            if (found) {
                response.add(priceItem);
            }
        }
        long stop = new Date().getTime();

        System.out.println(response.size() + " was found for " + (stop-start) + "ms");

        return response;
    }

    public String getProgramRequest(String name) {
        return wordsWorker.generateSearchQuery(name);
    }
}
