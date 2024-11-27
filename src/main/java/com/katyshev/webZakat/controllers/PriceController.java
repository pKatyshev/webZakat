package com.katyshev.webZakat.controllers;

import com.katyshev.webZakat.dto.UiDTO;
import com.katyshev.webZakat.services.OrderItemService;
import com.katyshev.webZakat.services.PriceItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
@RequestMapping("/price")
public class PriceController {

    private final PriceItemService priceItemService;
    private final OrderItemService orderItemService;

    @Autowired
    public PriceController(PriceItemService priceItemService, OrderItemService orderItemService) {
        this.priceItemService = priceItemService;
        this.orderItemService = orderItemService;
    }

    @GetMapping("/show")
    public String index(Model model,
                        @RequestParam(value = "page", required = false, defaultValue = "1") String page,
                        @RequestParam(value = "items_per_page", required = false, defaultValue = "30") String itemsPerPage ) {
        UiDTO uiDTO = new UiDTO();
        uiDTO.setPriceItemList(new ArrayList<>(priceItemService.findAllPerPage(page, itemsPerPage)));
        model.addAttribute("dto", uiDTO);
        return "price";
    }

    @GetMapping("/user_request")
    public String userRequest (Model model,
                               @RequestParam(value = "request", defaultValue = "") String request) {
        UiDTO uiDTO = priceItemService.userRequest(request);
        model.addAttribute("dto", uiDTO);
        return "price";
    }

    @GetMapping("/user_add")
    public String addToOrder(Model model,
                             @RequestParam(value = "request", defaultValue = "") String request,
                             @RequestParam(value = "id", required = false, defaultValue = "0") int priceItemId,
                             @RequestParam(value = "count", required = false, defaultValue = "0") String countStr) {

        int count = priceItemService.validateCount(priceItemId, countStr);

        orderItemService.save(priceItemId, count);
        priceItemService.setInOrder(priceItemId, count);

        UiDTO uiDTO = priceItemService.userRequest(request);

        model.addAttribute("dto", uiDTO);
        return "price";
    }
}
