package com.katyshev.webZakat.controllers;

import com.katyshev.webZakat.exceptions.FileNotFoundException;
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
    private final Engine engine;
    private final UnikoLecItemService unikoLecItemService;

    @Autowired
    public ImportController(Engine engine, UnikoLecItemService unikoLecItemService) {
        this.engine = engine;
        this.unikoLecItemService = unikoLecItemService;
    }

    @GetMapping("/query")
    public String importQuery() {
        engine.importNewUnikoQuery();
        return "redirect:/";
    }

    @GetMapping("/prices")
    public String importPrices() {
        System.out.println("import prices now");
        engine.importAllPrices();
        return "redirect:/";
    }

    @ExceptionHandler
    private String handle(FileNotFoundException e) {
        System.out.println(e.getMessage());
        return "redirect:/";
    }
}
