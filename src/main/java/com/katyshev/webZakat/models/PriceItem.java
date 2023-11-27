package com.katyshev.webZakat.models;

import jakarta.persistence.*;

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
    private float price;
    private int mark;
    private int inOrder;

    public PriceItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getCodePst() {
        return codePst;
    }

    public void setCodePst(String codePst) {
        this.codePst = codePst;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMnn() {
        return mnn;
    }

    public void setMnn(String mnn) {
        this.mnn = mnn;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getQntPack() {
        return qntPack;
    }

    public void setQntPack(int qntPack) {
        this.qntPack = qntPack;
    }

    public String getEan13() {
        return ean13;
    }

    public void setEan13(String ean13) {
        this.ean13 = ean13;
    }

    public double getNds() {
        return nds;
    }

    public void setNds(double nds) {
        this.nds = nds;
    }

    public String getgDate() {
        return gDate;
    }

    public void setgDate(String gDate) {
        this.gDate = gDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public int getInOrder() {
        return inOrder;
    }

    public void setInOrder(int inOrder) {
        this.inOrder = inOrder;
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
