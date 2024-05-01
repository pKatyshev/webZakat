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


@Log
@Controller
@RequestMapping("/zakaz")
public class ZakazController {
    private final UnikoLecItemService unikoLecItemService;
    private final PriceItemService priceItemService;
    private final OrderItemService orderItemService;

    @Autowired
    public ZakazController(UnikoLecItemService unikoLecItemService,
                           PriceItemService priceItemService,
                           OrderItemService orderItemService) {
        this.unikoLecItemService = unikoLecItemService;
        this.priceItemService = priceItemService;
        this.orderItemService = orderItemService;
    }

    @GetMapping("/next")
    public String next(Model model,
                       @RequestParam(value = "position", required = false, defaultValue = "0")
                       int positionNumber) {

        positionNumber++;
        UiDTO uiDTO = unikoLecItemService.nextRequest(positionNumber, null);

        model.addAttribute("dto", uiDTO);
        return "zakaz";
    }


    @GetMapping("/previous")
    public String previous(Model model,
                       @RequestParam(value = "position", required = false, defaultValue = "0") int positionNumber) {


        positionNumber--;
        UiDTO uiDTO = unikoLecItemService.nextRequest(positionNumber, null);

        model.addAttribute("dto", uiDTO);
        return "zakaz";
    }

    @GetMapping("/user_request")
    public String userRequest(Model model,
                              @RequestParam(value = "position", required = false, defaultValue = "0") int positionNumber,
                              @RequestParam(value = "request", required = false, defaultValue = "") String userRequest) {

        UiDTO uiDTO = unikoLecItemService.nextRequest(positionNumber, userRequest);

        model.addAttribute("dto", uiDTO);
        return "zakaz";

    }


    @GetMapping("/user_add")
    public String addToOrder(Model model,
                             @RequestParam(value = "position", required = false, defaultValue = "0") int position,
                             @RequestParam(value = "id", required = false, defaultValue = "0") int priceItemId,
                             @RequestParam(value = "request", required = false, defaultValue = "") String progRequest,
                             @RequestParam(value = "count", required = false, defaultValue = "0") String countStr) {

        int count = priceItemService.validateCount(priceItemId, countStr);

        orderItemService.save(priceItemId, count);
        priceItemService.setInOrder(priceItemId, count);

        UiDTO uiDTO = unikoLecItemService.nextRequest(position, progRequest);

        model.addAttribute("dto", uiDTO);
        return "zakaz";
    }
}