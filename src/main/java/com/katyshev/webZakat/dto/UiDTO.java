package com.katyshev.webZakat.dto;

import com.katyshev.webZakat.models.PriceItem;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class UiDTO {
    private int position;
    private String name;
    private List<PriceItem> priceItemList;
    private String request;
    private BigDecimal totalAmount;
    private float requestCount;
}
