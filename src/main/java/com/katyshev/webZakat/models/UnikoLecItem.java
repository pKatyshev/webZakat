package com.katyshev.webZakat.models;

import jakarta.persistence.*;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderGroup() {
        return orderGroup;
    }

    public void setOrderGroup(int orderGroup) {
        this.orderGroup = orderGroup;
    }

    public int getTmc() {
        return tmc;
    }

    public void setTmc(int tmc) {
        this.tmc = tmc;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getOrdered() {
        return ordered;
    }

    public void setOrdered(int ordered) {
        this.ordered = ordered;
    }

    public String[] getArray() {
        String[] strings = new String[6];
        strings[0] = String.valueOf(orderGroup);
        strings[1] = String.valueOf(tmc);
        strings[2] = name;
        strings[3] = factory;
        strings[4] = String.valueOf(quantity);
        strings[5] = String.valueOf(ordered);
        return strings;
    }

    @Override
    public String toString() {
        return "LecItem{"+ " " + orderGroup + " \t" + tmc +  " \t" + quantity + " \t" + name +"}";
    }

}
