package com.katyshev.webZakat.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @Column(name = "order_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "price_item_id", referencedColumnName = "id")
    private PriceItem priceItem;

    private int inOrder;
    private LocalDateTime createTime;
    private LocalDateTime exportTime;

    public OrderItem() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return id == orderItem.id && inOrder == orderItem.inOrder && Objects.equals(priceItem, orderItem.priceItem) && Objects.equals(createTime, orderItem.createTime) && Objects.equals(exportTime, orderItem.exportTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, priceItem, inOrder, createTime, exportTime);
    }
}
