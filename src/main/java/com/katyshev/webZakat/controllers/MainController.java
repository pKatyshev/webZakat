package com.katyshev.webZakat.controllers;

import com.katyshev.webZakat.exceptions.FileNotFoundException;
import com.katyshev.webZakat.services.UnikoLecItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    private final UnikoLecItemService unikoLecItemService;

    @Autowired
    public MainController(UnikoLecItemService unikoLecItemService) {
        this.unikoLecItemService = unikoLecItemService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("list", unikoLecItemService.findAll());
        return "mainPage";
    }

    @GetMapping("/clear_query")
    public String clearQuery() {
        unikoLecItemService.deleteAll();
        return "redirect:/";
    }

    @GetMapping("/delete_item")
    public String deleteItem(@RequestParam("id") int id) {
        unikoLecItemService.deleteById(id);
        return "redirect:/";
    }
}
