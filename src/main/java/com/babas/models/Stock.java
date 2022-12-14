package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Entity(name = "stock_tbl")
public class Stock extends Babas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne
    private Product product;
    @NotNull
    @ManyToOne
    private Branch branch;
    @NotNull
    private Integer quantity=0;
    private Date created;
    private Date updated;
    private Integer onRental=0;
    private Integer onStock=0;
    private Integer timesRented=0;
    private Integer onReserve=0;
    private boolean active=true;

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

    public Integer getOnRental() {
        return onRental;
    }

    public void setOnRental(Integer onRental) {
        this.onRental = onRental;
    }

    public Integer getOnStock() {
        return onStock;
    }

    public void setOnStock(Integer onStock) {
        this.onStock = onStock;
    }

    public Integer getTimesRented() {
        return timesRented;
    }

    public void setTimesRented(Integer timesRented) {
        this.timesRented = timesRented;
    }

    public Integer getOnReserve() {
        return onReserve;
    }

    public void setOnReserve(Integer onReserve) {
        this.onReserve = onReserve;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void save() {
        if(created==null){
            created=new Date();
        }
        updated=new Date();
        super.save();
        product.refresh();
        product.calculateStockTotal();
        product.save();
    }
}
