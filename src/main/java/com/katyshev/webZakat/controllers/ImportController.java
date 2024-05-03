package com.katyshev.webZakat.controllers;

import com.katyshev.webZakat.exceptions.FileNotFoundException;
import com.katyshev.webZakat.exceptions.ImporterException;
import com.katyshev.webZakat.services.PriceItemService;
import com.katyshev.webZakat.services.UnikoLecItemService;
import com.katyshev.webZakat.utils.Engine;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log
@Controller
@RequestMapping("/import")
public class ImportController {
    private final UnikoLecItemService unikoLecItemService;
    private final PriceItemService priceItemService;

    @Autowired
    public ImportController(UnikoLecItemService unikoLecItemService, PriceItemService priceItemService) {
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
        priceItemService.importAllPrices();
        return "redirect:/";
    }

    @ExceptionHandler
    private String handle(Model model, FileNotFoundException e) {
        model.addAttribute("message", e.getMessage());
        return "error";
    }

    @ExceptionHandler(ImporterException.class)
    private String handle2(Model model,ImporterException e) {
        model.addAttribute("message", e.getMessage());
        return "error";
    }
}
