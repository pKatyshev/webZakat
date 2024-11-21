package com.katyshev.webZakat.controllers;

import com.katyshev.webZakat.dto.ServerResponse;
import com.katyshev.webZakat.dto.UiDTO;
import com.katyshev.webZakat.dto.UserAddForm;
import com.katyshev.webZakat.services.OrderItemService;
import com.katyshev.webZakat.services.PriceItemService;
import com.katyshev.webZakat.services.UnikoLecItemService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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
                       @RequestParam(value = "position", required = false, defaultValue = "1")
                       int positionNumber) {
        UiDTO uiDTO = unikoLecItemService.nextRequest(positionNumber, null, true);

        model.addAttribute("dto", uiDTO);
        return "zakaz";
    }


    @GetMapping("/previous")
    public String previous(Model model,
                       @RequestParam(value = "position", required = false, defaultValue = "0") int positionNumber) {

        UiDTO uiDTO = unikoLecItemService.nextRequest(positionNumber, null, false);

        model.addAttribute("dto", uiDTO);
        return "zakaz";
    }

    @GetMapping("/user_request")
    public String userRequest(Model model,
                              @RequestParam(value = "position", required = false, defaultValue = "0") int positionNumber,
                              @RequestParam(value = "request", required = false, defaultValue = "") String userRequest) {

        UiDTO uiDTO = unikoLecItemService.nextRequest(positionNumber, userRequest, true);

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
        unikoLecItemService.setOrdered(position, count);

        UiDTO uiDTO = unikoLecItemService.nextRequest(position, progRequest, true);

        model.addAttribute("dto", uiDTO);
        return "zakaz";
    }

    @PostMapping("/user_add-ajax")
    public @ResponseBody ServerResponse userAddAjax(@RequestBody UserAddForm userAddForm) {
        int countStr = userAddForm.getCountStr();
        int position = userAddForm.getPosition();
        int priceItemId = userAddForm.getPriceItemId();

        int count = priceItemService.validateCount(priceItemId, String.valueOf(countStr));
        try {
            orderItemService.save(priceItemId, count);
            priceItemService.setInOrder(priceItemId, count);
            unikoLecItemService.setOrdered(position, count);
            return new ServerResponse("good");
        } catch (Exception e) {
            return new ServerResponse(e.getMessage());
        }
    }
}