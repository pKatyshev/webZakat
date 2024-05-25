package com.katyshev.webZakat.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "uniko_lec_item")
public class UnikoLecItem {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "order_group")
    private int orderGroup;

    @Column(name = "tmc")
    private int tmc;

    @Column(name = "quantity")
    private float quantity;

    @Column(name = "name")
    private String name;

    @Column(name = "factory")
    private String factory;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "ordered")
    private int ordered;

    public UnikoLecItem() {

    }

    public UnikoLecItem(int orderGroup, int tmc, float quantity, String name, String factory) {
        this.orderGroup = orderGroup;
        this.tmc = tmc;
        this.quantity = quantity;
        this.name = name;
        this.factory = factory;
    }

    public UnikoLecItem(int id, int orderGroup, int tmc, float quantity, String name, String factory, String shortName, int ordered) {
        this.id = id;
        this.orderGroup = orderGroup;
        this.tmc = tmc;
        this.quantity = quantity;
        this.name = name;
        this.factory = factory;
        this.shortName = shortName;
        this.ordered = ordered;
    }

    @Override
    public String toString() {
        return "LecItem{"+ " " + orderGroup + " \t" + tmc +  " \t" + quantity + " \t" + name +"}";
    }

}
