package com.katyshev.webZakat.controllers;

import com.katyshev.webZakat.services.OrderItemService;
import com.katyshev.webZakat.services.PriceItemService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log
@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderItemService orderItemService;
    private final PriceItemService priceItemService;

    public OrderController(OrderItemService orderItemService, PriceItemService priceItemService) {
        this.orderItemService = orderItemService;
        this.priceItemService = priceItemService;
    }

    @GetMapping("/show")
    public String index(Model model) {
        model.addAttribute("list", orderItemService.findAll());
        model.addAttribute("distrs", orderItemService.getDistrSumList());
        return "order";
    }

    @GetMapping("/edit")
    public String edit(Model model,
                       @RequestParam(value = "price_item_id", required = false, defaultValue = "0") int priceItemId,
                       @RequestParam(value = "count", required = false, defaultValue = "0") String countStr) {

        int count = priceItemService.validateCount(priceItemId, countStr);

        orderItemService.edit(priceItemId, count);
        priceItemService.setInOrder(priceItemId, count);

        model.addAttribute("list", orderItemService.findAll());
        model.addAttribute("distrs", orderItemService.getDistrSumList());
        return "order";
    }

    @GetMapping("/delete")
    public String delete(Model model,
                         @RequestParam(value = "price_item_id", required = false) int priceItemId) {

        orderItemService.delete(priceItemId);
        priceItemService.setInOrder(priceItemId, 0);

        model.addAttribute("list", orderItemService.findAll());
        model.addAttribute("distrs", orderItemService.getDistrSumList());
        return "order";
    }

    @GetMapping("/export")
    public String export() {
        orderItemService.export();
        return "redirect:/order/show";
    }
}
