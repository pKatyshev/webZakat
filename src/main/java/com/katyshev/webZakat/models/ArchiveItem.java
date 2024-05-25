package com.katyshev.webZakat.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "archive_item")
public class ArchiveItem {

    @Id
    @Column(name = "archive_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int orderNumber;
    private String priceName;
    private BigDecimal price;
    private int quantity;
    private LocalDateTime exportTime;

    public ArchiveItem() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArchiveItem that = (ArchiveItem) o;
        return orderNumber == that.orderNumber && quantity == that.quantity && priceName.equals(that.priceName) && Objects.equals(price, that.price) && Objects.equals(exportTime, that.exportTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, priceName, price, quantity, exportTime);
    }
}
