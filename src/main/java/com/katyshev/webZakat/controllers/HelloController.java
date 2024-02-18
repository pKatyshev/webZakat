package com.katyshev.webZakat.controllers;

import com.katyshev.webZakat.repositories.CustomPriceItemRepository;
import com.katyshev.webZakat.repositories.UnikoLecItemRepository;
import com.katyshev.webZakat.utils.Engine;
import com.katyshev.webZakat.utils.WordsWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Deprecated
@Controller
public class HelloController {

    private final UnikoLecItemRepository unikoLecItemRepository;
    private final CustomPriceItemRepository customPriceItemRepository;
    private final Engine engine;
    private final WordsWorker wordsWorker;

    @Autowired
    public HelloController(UnikoLecItemRepository unikoLecItemRepository, CustomPriceItemRepository customPriceItemRepository, Engine engine, WordsWorker wordsWorker) {
        this.unikoLecItemRepository = unikoLecItemRepository;
        this.customPriceItemRepository = customPriceItemRepository;
        this.engine = engine;
        this.wordsWorker = wordsWorker;
    }

    @GetMapping("/hello")
    public String hello() {

        System.out.println("оно работает!!!");

        unikoLecItemRepository.findAll().forEach(System.out::println);

        return "hello";
    }

    @GetMapping("/test")
    public String test() {
        System.out.println("===TEST===\n");

//        List<PriceItem> list = engine.getPriceListForStringCustom("Энап");
//
//        list.forEach(System.out::println);

//        System.out.println("_"+wordsWorker.getSqlParameter( "Aqua 1 1 30 ") + "_");

        int size = customPriceItemRepository.findAllByQuery("").size();
        System.out.println(size);

        return "hello";
    }
}
