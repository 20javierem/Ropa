package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity(name = "stock_tbl")
public class Stock extends Babas {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @NotNull
    @ManyToOne
    private Product product;
    @NotNull
    @ManyToOne
    private Branch branch;
    @NotNull
    private Integer quantity=0;
    private Date created=new Date();
    private Date updated;

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public void save() {
        updated=new Date();
        super.save();
    }
}
