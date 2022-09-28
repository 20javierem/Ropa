package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity(name = "movement_tbl")
public class Movement extends Babas {

    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    private Double amount=0.0;
    @ManyToOne
    @NotNull(message = "Caja")
    private BoxSession boxSession;
    private Date created;
    private Date updated;
    private String description;
    private boolean entrance;

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public BoxSession getBoxSesion() {
        return boxSession;
    }

    public void setBoxSesion(BoxSession boxSession) {
        this.boxSession = boxSession;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEntrance() {
        return entrance;
    }

    public void setEntrance(boolean entrance) {
        this.entrance = entrance;
    }

    @Override
    public void save() {
        if(created==null){
            created=new Date();
        }
        if(!isEntrance()){
            amount=-amount;
        }
        updated=new Date();
        super.save();
        boxSession.getMovements().add(this);
        boxSession.calculateTotals();
    }
}
