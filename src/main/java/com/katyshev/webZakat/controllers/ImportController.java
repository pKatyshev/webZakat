package com.katyshev.webZakat.controllers;

import com.katyshev.webZakat.exceptions.FileNotFoundException;
import com.katyshev.webZakat.exceptions.ImporterException;
import com.katyshev.webZakat.services.PriceItemService;
import com.katyshev.webZakat.services.UnikoLecItemService;
import com.katyshev.webZakat.utils.Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/import")
public class ImportController {
    private final UnikoLecItemService unikoLecItemService;
    private final PriceItemService priceItemService;

    @Autowired
    public ImportController(Engine engine, UnikoLecItemService unikoLecItemService, PriceItemService priceItemService) {
        this.priceItemService = priceItemService;
        this.unikoLecItemService = unikoLecItemService;
    }

    @GetMapping("/query")
    public String importQuery() {
        unikoLecItemService.importNewUnikoQuery();
        return "redirect:/";
    }

    @GetMapping("/prices")
    public String importPrices() {
        System.out.println("import prices now");
        priceItemService.importAllPrices();
        return "redirect:/";
    }

    @ExceptionHandler
    private String handle(FileNotFoundException e) {
        System.out.println(e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler
    private String handle2(ImporterException e) {
        System.out.println(e.getMessage());
        return "redirect:/";
    }
}
