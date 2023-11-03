package com.katyshev.webZakat.controllers;

import com.katyshev.webZakat.services.PriceItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/price")
public class PriceController {

    private final PriceItemService priceItemService;

    @Autowired
    public PriceController(PriceItemService priceItemService) {
        this.priceItemService = priceItemService;
    }

    @GetMapping("/show")
    public String index(Model model,
                        @RequestParam(value = "page", required = false, defaultValue = "1") String page,
                        @RequestParam(value = "items_per_page", required = false, defaultValue = "30") String itemsPerPage ) {


        model.addAttribute("priceList", priceItemService.findAllPerPage(page, itemsPerPage));
        return "price";
    }
}
