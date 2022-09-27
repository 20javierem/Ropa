package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity(name = "rental_tbl")
public class Rental extends Babas {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @ManyToOne
    @NotNull(message = "Caja")
    private BoxSesion boxSesion;
    private Double amount=0.0;
    @ManyToOne
    private Client client;
    private Date created;
    private Date updated;

    public Long getId() {
        return id;
    }

    public BoxSesion getBoxSesion() {
        return boxSesion;
    }

    public void setBoxSesion(BoxSesion boxSesion) {
        this.boxSesion = boxSesion;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public void save() {
        if(created==null){
            created=new Date();
        }
        super.save();
    }
}
