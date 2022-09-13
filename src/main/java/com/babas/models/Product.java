package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.*;

@Entity(name = "product_tbl")
public class Product extends Babas {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @ManyToOne
    @NotNull(message = "Estilo")
    private Style style;
    @ManyToOne
    @NotNull(message = "Color")
    private Color color;
    @ManyToOne
    @NotNull(message = "Género")
    private Sex sex;
    @ManyToOne
    @NotNull(message = "Talla")
    private Size size;
    @ElementCollection
    @CollectionTable(name="Images", joinColumns=@JoinColumn(name="product_id"))
    @Column(name="image")
    private List<String> images=new ArrayList<>();
    private Date created=new Date();
    private Date updated;
    private Integer stockTotal=0;
    private boolean active=true;

    public Long getId() {
        return id;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }


    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Integer getStockTotal() {
        return stockTotal;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getImages() {
        return images;
    }

    private void calculateStockTotal(){

    }

    @Override
    public void save() {
        updated=new Date();
        calculateStockTotal();
        super.save();
    }
}
