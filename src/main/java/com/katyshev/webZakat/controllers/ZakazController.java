package com.katyshev.webZakat.controllers;

import com.katyshev.webZakat.exceptions.PositionIndexOfBound;
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
                       @RequestParam(value = "position", required = false, defaultValue = "0") String position) {
        int positionNumber = Integer.parseInt(position);
        ++positionNumber;

        UnikoLecItem unikoItem;
        try {
            unikoItem = unikoLecItemService.findById(positionNumber);
        } catch (PositionIndexOfBound e) {
            positionNumber = 1;
            unikoItem = unikoLecItemService.findById(positionNumber);
        }
        return enrichModel(model, unikoItem);
    }

    @GetMapping("/previous")
    public String previous(Model model,
                       @RequestParam(value = "position", required = false, defaultValue = "0") String position) {
        int positionNumber = Integer.parseInt(position);
        --positionNumber;

        UnikoLecItem unikoItem;
        try {
            unikoItem = unikoLecItemService.findById(positionNumber);
        } catch (PositionIndexOfBound e) {
            positionNumber = 1;
            unikoItem = unikoLecItemService.findById(positionNumber);
        }
        return enrichModel(model, unikoItem);
    }

    private String enrichModel(Model model, UnikoLecItem unikoItem) {
        List<PriceItem> list = engine.getPriceListForUnikoItem(unikoItem);

        model.addAttribute("position", unikoItem.getId());
        model.addAttribute("name", unikoItem.getName());
        model.addAttribute("list", list);
        model.addAttribute("prog_req", engine.getProgramRequest(unikoItem.getName()));
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
            list = engine.getPriceListForString(userRequest);
        }

        UnikoLecItem unikoItem;
        try {
            unikoItem = unikoLecItemService.findById(positionNumber);
        } catch (PositionIndexOfBound e) {
            positionNumber = 1;
            unikoItem = unikoLecItemService.findById(positionNumber);
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
        int count = Integer.parseInt(countStr);

        UnikoLecItem unikoItem;
        try {
            unikoItem = unikoLecItemService.findById(positionNumber);
        } catch (PositionIndexOfBound e) {
            positionNumber = 1;
            unikoItem = unikoLecItemService.findById(positionNumber);
        }

        List<PriceItem> list = engine.getPriceListForString(progRequest);

        if (count <= 0) {
            orderItemService.delete(priceItemId);
        } else {
            orderItemService.save(priceItemId, count);
        }

        priceItemService.setInOrder(priceItemId, count);

        model.addAttribute("position", unikoItem.getId());
        model.addAttribute("name", unikoItem.getName());
        model.addAttribute("list", list);
        model.addAttribute("user_req", progRequest);
        model.addAttribute("prog_req", progRequest);
        return "zakaz";
    }
}
