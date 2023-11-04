package com.katyshev.webZakat.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @Column(name = "order_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "order_number")
    private int orderNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "price")
    private double price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "distributor")
    private String distributor;

    @Column(name ="price_item_id")
    private int priceItemId;

    public OrderItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public int getPriceItemId() {
        return priceItemId;
    }

    public void setPriceItemId(int priceItemId) {
        this.priceItemId = priceItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return id == orderItem.id && orderNumber == orderItem.orderNumber && Double.compare(orderItem.price, price) == 0 && quantity == orderItem.quantity && priceItemId == orderItem.priceItemId && Objects.equals(name, orderItem.name) && Objects.equals(manufacturer, orderItem.manufacturer) && Objects.equals(distributor, orderItem.distributor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderNumber, name, manufacturer, price, quantity, distributor, priceItemId);
    }
}
