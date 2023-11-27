package com.katyshev.webZakat.controllers;

import com.katyshev.webZakat.exceptions.UnikoItemListIsEmptyException;
import com.katyshev.webZakat.models.PriceItem;
import com.katyshev.webZakat.models.UnikoLecItem;
import com.katyshev.webZakat.services.OrderItemService;
import com.katyshev.webZakat.services.PriceItemService;
import com.katyshev.webZakat.services.UnikoLecItemService;
import com.katyshev.webZakat.utils.Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/zakaz")
public class ZakazController {
    private final UnikoLecItemService unikoLecItemService;
    private final PriceItemService priceItemService;
    private final OrderItemService orderItemService;
    private final Engine engine;

    @Autowired
    public ZakazController(UnikoLecItemService unikoLecItemService,
                           PriceItemService priceItemService,
                           OrderItemService orderItemService,
                           Engine engine) {
        this.unikoLecItemService = unikoLecItemService;
        this.priceItemService = priceItemService;
        this.orderItemService = orderItemService;
        this.engine = engine;
    }

    @GetMapping("/next")
    public String next(Model model,
                       @RequestParam(value = "position", required = false, defaultValue = "0")
                       String position) {
        int positionNumber = Integer.parseInt(position);
        ++positionNumber;

        UnikoLecItem unikoItem;
        try {
            unikoItem= unikoLecItemService.findNextById(positionNumber);
        } catch (UnikoItemListIsEmptyException e) {
            model.addAttribute("list", new ArrayList<>());
            return "zakaz";
        }

        String programRequest = engine.getProgramRequest(unikoItem.getName());
        long start = new Date().getTime();
//        List<PriceItem> list = engine.getPriceListForString(programRequest);
        List<PriceItem> list = engine.getPriceListForStringCustom(programRequest);
        long stop = new Date().getTime();
        System.out.println("Time to search: " + (stop - start) + "ms");

        model.addAttribute("position", unikoItem.getId());
        model.addAttribute("name", unikoItem.getName());
        model.addAttribute("list", list);
        model.addAttribute("prog_req", programRequest);
        return "zakaz";
    }

    @GetMapping("/previous")
    public String previous(Model model,
                       @RequestParam(value = "position", required = false, defaultValue = "0") String position) {

        int positionNumber = Integer.parseInt(position);
        --positionNumber;

        UnikoLecItem unikoItem;
        try {
            unikoItem= unikoLecItemService.findNextById(positionNumber);
        } catch (UnikoItemListIsEmptyException e) {
            model.addAttribute("list", new ArrayList<>());
            return "zakaz";
        }

        String programRequest = engine.getProgramRequest(unikoItem.getName());
        List<PriceItem> list = engine.getPriceListForStringCustom(programRequest);

        model.addAttribute("position", unikoItem.getId());
        model.addAttribute("name", unikoItem.getName());
        model.addAttribute("list", list);
        model.addAttribute("prog_req", programRequest);
        return "zakaz";
    }

    @GetMapping("/user_request")
    public String userRequest(Model model,
                              @RequestParam(value = "position", required = false, defaultValue = "0") String position,
                              @RequestParam(value = "user_req", required = false, defaultValue = "") String userRequest) {
        int positionNumber = Integer.parseInt(position);
        List<PriceItem> list;

        if (userRequest.equals("")) {
            list = new ArrayList<>();
        } else {
            list = engine.getPriceListForStringCustom(userRequest);
        }

        UnikoLecItem unikoItem;
        try {
            unikoItem = unikoLecItemService.findNextById(positionNumber);
        } catch (UnikoItemListIsEmptyException e) {
            model.addAttribute("list", list);
            model.addAttribute("user_req", userRequest);
            model.addAttribute("prog_req", userRequest);
            return "zakaz";
        }

        model.addAttribute("position", unikoItem.getId());
        model.addAttribute("name", unikoItem.getName());
        model.addAttribute("list", list);
        model.addAttribute("user_req", userRequest);
        model.addAttribute("prog_req", userRequest);
        return "zakaz";
    }

    @GetMapping("/user_add")
    public String addToOrder(Model model,
                             @RequestParam(value = "position", required = false, defaultValue = "0") String position,
                             @RequestParam(value = "id", required = false, defaultValue = "0") String id,
                             @RequestParam(value = "prog_req", required = false, defaultValue = "") String progRequest,
                             @RequestParam(value = "count", required = false, defaultValue = "0") String countStr) {

        int positionNumber = Integer.parseInt(position);
        int priceItemId = Integer.parseInt(id);
        int count = priceItemService.validateCount(priceItemId, countStr);

        orderItemService.save(priceItemId, count);
        priceItemService.setInOrder(priceItemId, count);

        List<PriceItem> list = engine.getPriceListForStringCustom(progRequest);

        UnikoLecItem unikoItem;
        try {
            unikoItem = unikoLecItemService.findNextById(positionNumber);
        } catch (UnikoItemListIsEmptyException e) {
            model.addAttribute("list", list);
            model.addAttribute("user_req", progRequest);
            model.addAttribute("prog_req", progRequest);
            return "zakaz";
        }

        model.addAttribute("position", unikoItem.getId());
        model.addAttribute("name", unikoItem.getName());
        model.addAttribute("list", list);
        model.addAttribute("user_req", progRequest);
        model.addAttribute("prog_req", progRequest);
        return "zakaz";
    }
}
