package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "presentation_tbl")
public class Presentation extends Babas {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @NotNull
    @ManyToOne
    private Style style;
    @NotNull
    private Integer quantity;
    @NotEmpty(message = "Precios")
    @OneToMany(mappedBy = "presentation")
    private List<Price> prices =new ArrayList<>();
    private Date created=new Date();
    private Date updated;
    @ManyToOne
    private Price priceDefault;

    public Presentation(Style style){
        this.style=style;
    }

    public Presentation() {

    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public Long getId() {
        return id;
    }

    public Style getStyle() {
        return style;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public Price getPriceDefault() {
        return priceDefault;
    }

    public void setPriceDefault(Price priceDefault) {
        this.priceDefault = priceDefault;
    }

    @Override
    public void save() {
        updated=new Date();
        Price price=getPriceDefault();
        setPriceDefault(null);
        super.save();
        getPrices().forEach(Price::save);
        setPriceDefault(price);
        super.save();
    }
}
