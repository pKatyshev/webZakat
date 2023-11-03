package com.katyshev.webZakat.controllers;

import com.katyshev.webZakat.dao.UnikoLecItemDAO;
import com.katyshev.webZakat.repositories.UnikoLecItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HelloController {

    private final UnikoLecItemDAO unikoLecItemDAO;
    private final UnikoLecItemRepository unikoLecItemRepository;

    @Autowired
    public HelloController(UnikoLecItemDAO unikoLecItemDAO, UnikoLecItemRepository unikoLecItemRepository) {
        this.unikoLecItemDAO = unikoLecItemDAO;
        this.unikoLecItemRepository = unikoLecItemRepository;
    }

    @GetMapping("/hello")
    public String hello() {

        System.out.println("оно работает!!!");

        unikoLecItemRepository.findAll().forEach(System.out::println);

        return "hello";
    }
}
