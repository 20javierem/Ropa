package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity(name = "price_tbl")
public class Price extends Babas {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @ManyToOne
    private Presentation presentation;
    private Double price;
    private Date created;
    private Date updated;
    private boolean isDefault=false;

    public Price(Presentation presentation){
        this.presentation=presentation;
        created=new Date();
    }

    public Price() {

    }

    public Long getId() {
        return id;
    }

    public Presentation getPresentation() {
        return presentation;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public void save() {
        updated=new Date();
        super.save();
    }
}
