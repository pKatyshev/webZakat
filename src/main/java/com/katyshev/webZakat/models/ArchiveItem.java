package com.katyshev.webZakat.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "archive_item")
public class ArchiveItem {

    @Id
    @Column(name = "archive_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int orderNumber;
    private String priceName;
    private float price;
    private int quantity;
    private LocalDateTime exportTime;

    public ArchiveItem() {
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

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getExportTime() {
        return exportTime;
    }

    public void setExportTime(LocalDateTime exportTime) {
        this.exportTime = exportTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArchiveItem that = (ArchiveItem) o;
        return orderNumber == that.orderNumber && Float.compare(that.price, price) == 0 && quantity == that.quantity && priceName.equals(that.priceName) && Objects.equals(exportTime, that.exportTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, priceName, price, quantity, exportTime);
    }
}
