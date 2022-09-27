package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "box_sesion_tbl")
public class BoxSesion extends Babas {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    @NotNull(message = "Caja")
    private Box box;
    private Double totalSales=0.0;
    private Double totalRentals=0.0;
    private Double totalReserves=0.0;
    private Double totalMovements=0.0;
    private Double amountInitial=0.0;
    private Double amountToDelivered=0.0;
    private Double amountDelivered=0.0;
    @OneToMany(mappedBy = "boxSesion")
    private List<Sale> sales=new ArrayList<>();
    @OneToMany(mappedBy = "boxSesion")
    private List<Rental> rentals=new ArrayList<>();
    @OneToMany(mappedBy = "boxSesion")
    private List<Reserve> reserves=new ArrayList<>();
    @OneToMany(mappedBy = "boxSesion")
    private List<Movement> movements=new ArrayList<>();
    private Date created;
    private Date updated;
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public Double getAmountInitial() {
        return amountInitial;
    }

    public void setAmountInitial(Double amountInitial) {
        this.amountInitial = amountInitial;
        this.amountToDelivered=amountInitial;
    }

    public Double getAmountToDelivered() {
        return amountToDelivered;
    }

    public void setAmountToDelivered(Double amountToDelivered) {
        this.amountToDelivered = amountToDelivered;
    }

    public Double getAmountDelivered() {
        return amountDelivered;
    }

    public void setAmountDelivered(Double amountDelivered) {
        this.amountDelivered = amountDelivered;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }

    public List<Reserve> getReserves() {
        return reserves;
    }

    public void setReserves(List<Reserve> reserves) {
        this.reserves = reserves;
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

    public List<Movement> getMovements() {
        return movements;
    }

    @Override
    public void save() {
        if(created==null){
            created=new Date();
        }
        super.save();
    }

    public Double getTotalSales() {
        return totalSales;
    }

    public Double getTotalRentals() {
        return totalRentals;
    }

    public Double getTotalReserves() {
        return totalReserves;
    }

    public Double getTotalMovements() {
        return totalMovements;
    }

    public void calculateTotals() {
        amountToDelivered=amountInitial;
        totalSales=0.0;
        getSales().forEach(sale -> totalSales+=sale.getTotalCurrent());
        totalRentals=0.0;
        getRentals().forEach(rental -> totalRentals+=rental.getAmount());
        totalReserves=0.0;
        getReserves().forEach(reserve -> totalReserves+=reserve.getTotal());
        totalMovements=0.0;
        getMovements().forEach(movement -> totalMovements+=movement.getAmount());
        amountToDelivered+=totalSales+totalRentals+totalReserves+totalMovements;
        save();
    }
}
