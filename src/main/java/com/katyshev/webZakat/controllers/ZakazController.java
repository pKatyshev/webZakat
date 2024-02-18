package com.katyshev.webZakat.controllers;

import com.katyshev.webZakat.dto.UiDTO;
import com.katyshev.webZakat.exceptions.UnikoItemListIsEmptyException;
import com.katyshev.webZakat.models.PriceItem;
import com.katyshev.webZakat.models.UnikoLecItem;
import com.katyshev.webZakat.services.OrderItemService;
import com.katyshev.webZakat.services.PriceItemService;
import com.katyshev.webZakat.services.UnikoLecItemService;
import com.katyshev.webZakat.utils.Engine;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Log
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
                       int positionNumber) {

        UiDTO uiDTO;
        positionNumber++;

        try {
            uiDTO = unikoLecItemService.nextRequest(positionNumber, null);
        } catch (UnikoItemListIsEmptyException e) {
            model.addAttribute("list", new ArrayList<>());
            return "zakaz";
        }

        model.addAttribute("position", uiDTO.getPosition());
        model.addAttribute("name", uiDTO.getName());
        model.addAttribute("list", uiDTO.getPriceItemList());
        model.addAttribute("prog_req", uiDTO.getRequest());
        return "zakaz";
    }

    @GetMapping("/test_next")
    public String testNext(Model model,
                       @RequestParam(value = "position", required = false, defaultValue = "0")
                       int positionNumber) {

        UiDTO uiDTO;
        positionNumber++;

        try {
            uiDTO = unikoLecItemService.nextRequest(positionNumber, null);
        } catch (UnikoItemListIsEmptyException e) {
            model.addAttribute("list", new ArrayList<>());
            return "zakaz";
        }

        model.addAttribute("dto", uiDTO);
        return "test_zakaz";
    }

    @GetMapping("/previous")
    public String previous(Model model,
                       @RequestParam(value = "position", required = false, defaultValue = "0") int positionNumber) {


        UiDTO uiDTO;
        --positionNumber;

        try {
            uiDTO = unikoLecItemService.nextRequest(positionNumber, null);
        } catch (UnikoItemListIsEmptyException e) {
            model.addAttribute("list", new ArrayList<>());
            return "zakaz";
        }

        model.addAttribute("position", uiDTO.getPosition());
        model.addAttribute("name", uiDTO.getName());
        model.addAttribute("list", uiDTO.getPriceItemList());
        model.addAttribute("prog_req", uiDTO.getRequest());
        return "zakaz";
    }

    @GetMapping("/user_request")
    public String userRequest(Model model,
                              @RequestParam(value = "position", required = false, defaultValue = "0") int positionNumber,
                              @RequestParam(value = "user_req", required = false, defaultValue = "") String userRequest) {

        UiDTO uiDTO;

        try {
            uiDTO = unikoLecItemService.nextRequest(positionNumber, userRequest);
        } catch (UnikoItemListIsEmptyException e) {
            model.addAttribute("list", new ArrayList<>());
            model.addAttribute("user_req", userRequest);
            model.addAttribute("prog_req", userRequest);
            return "zakaz";
        }

        model.addAttribute("position", uiDTO.getPosition());
        model.addAttribute("name", uiDTO.getName());
        model.addAttribute("list", uiDTO.getPriceItemList());
        model.addAttribute("user_req", uiDTO.getRequest());
        model.addAttribute("prog_req", uiDTO.getRequest());
        return "zakaz";
    }

    /**
     * todo
     * do it on JS-function
     */
    @Deprecated
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

        UiDTO uiDTO;

        try {
            uiDTO = unikoLecItemService.nextRequest(positionNumber, null);
        } catch (UnikoItemListIsEmptyException e) {
            model.addAttribute("list", new ArrayList<>());
            return "zakaz";
        }

        model.addAttribute("position", uiDTO.getPosition());
        model.addAttribute("name", uiDTO.getName());
        model.addAttribute("list", uiDTO.getPriceItemList());
        model.addAttribute("prog_req", uiDTO.getRequest());
        return "zakaz";
    }
}
