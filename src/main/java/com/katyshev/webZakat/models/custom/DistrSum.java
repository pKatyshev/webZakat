package com.katyshev.webZakat.models.custom;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class DistrSum {
    private String dist;
    private BigDecimal sum;

    public DistrSum() {
    }

    public DistrSum(String dist, BigDecimal sum) {
        this.dist = dist;
        this.sum = sum;
    }
}
