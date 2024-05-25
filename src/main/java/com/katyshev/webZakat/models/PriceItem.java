package com.katyshev.webZakat.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "price_item")
public class PriceItem {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String dist;
    private String codePst;
    private String name;
    private String mnn;
    private String country;
    private String manufacturer;
    private int qntPack;
    private String ean13;
    private double nds;
    private String gDate;
    private int quantity;
    private BigDecimal price;
    private int mark;
    private int inOrder;

    public PriceItem() {
    }

    @Override
    public String toString() {
        return "PriceItem{" +
                "id=" + id +
                ", dist='" + dist + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", inOrder=" + inOrder +
                '}';
    }
}
