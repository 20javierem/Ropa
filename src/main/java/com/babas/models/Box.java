package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.Date;

@Entity(name = "box_tbl")
public class Box extends Babas {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Branch branch;
    private Date created;
    private Date updated;
    private Double startingAmount=0.0;
    private Double amountDelivered=0.0;
    private Double amountToDeliver=0.0;

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Double getStartingAmount() {
        return startingAmount;
    }

    public void setStartingAmount(Double startingAmount) {
        this.startingAmount = startingAmount;
        this.amountToDeliver = startingAmount;
    }

    public Double getAmountDelivered() {
        return amountDelivered;
    }

    public void setAmountDelivered(Double amountDelivered) {
        this.amountDelivered = amountDelivered;
    }

    public Double getAmountToDeliver() {
        return amountToDeliver;
    }

    public void setAmountToDeliver(Double amountToDeliver) {
        this.amountToDeliver = amountToDeliver;
    }

    @Override
    public void save() {
        if(created==null){
            created=new Date();
        }
        super.save();
    }
}
