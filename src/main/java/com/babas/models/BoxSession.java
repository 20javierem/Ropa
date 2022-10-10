package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "box_session_tbl")
public class BoxSession extends Babas {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    @NotNull(message = "Caja")
    private Box box;
    private Double totalSalesCash=0.0;
    private Double totalSalesTransfer=0.0;
    private Double totalRentalsCash=0.0;
    private Double totalRentalsTransfer=0.0;
    private Double totalReservesCash=0.0;
    private Double totalReservesTransfer=0.0;
    private Double totalMovements=0.0;
    private Double amountInitial=0.0;
    private Double amountToDelivered=0.0;
    private Double amountDelivered=0.0;
    @OneToMany(mappedBy = "boxSession")
    @OrderBy(value = "id DESC")
    private List<Sale> sales=new ArrayList<>();
    @OneToMany(mappedBy = "boxSession")
    @OrderBy(value = "id DESC")
    private List<Rental> rentals=new ArrayList<>();
    @OneToMany(mappedBy = "boxSession")
    @OrderBy(value = "id DESC")
    private List<Reserve> reserves=new ArrayList<>();
    @OneToMany(mappedBy = "boxSession")
    @OrderBy(value = "id DESC")
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

    public Double getTotalSalesCash() {
        return totalSalesCash;
    }

    public void setTotalSalesCash(Double totalSalesCash) {
        this.totalSalesCash = totalSalesCash;
    }

    public Double getTotalSalesTransfer() {
        return totalSalesTransfer;
    }

    public void setTotalSalesTransfer(Double totalSalesTransfer) {
        this.totalSalesTransfer = totalSalesTransfer;
    }

    public Double getTotalRentalsCash() {
        return totalRentalsCash;
    }

    public void setTotalRentalsCash(Double totalRentalsCash) {
        this.totalRentalsCash = totalRentalsCash;
    }

    public Double getTotalRentalsTransfer() {
        return totalRentalsTransfer;
    }

    public void setTotalRentalsTransfer(Double totalRentalsTransfer) {
        this.totalRentalsTransfer = totalRentalsTransfer;
    }

    public Double getTotalReservesCash() {
        return totalReservesCash;
    }

    public void setTotalReservesCash(Double totalReservesCash) {
        this.totalReservesCash = totalReservesCash;
    }

    public Double getTotalReservesTransfer() {
        return totalReservesTransfer;
    }

    public void setTotalReservesTransfer(Double totalReservesTransfer) {
        this.totalReservesTransfer = totalReservesTransfer;
    }

    public Double getTotalMovements() {
        return totalMovements;
    }

    public void calculateTotals() {
        amountToDelivered=amountInitial;
        totalSalesCash=0.0;
        totalSalesTransfer=0.0;
        getSales().forEach(sale -> {
            if(sale.isCash()){
                totalSalesCash+=sale.getTotalCurrent();
            }else{
                totalSalesTransfer+=sale.getTotalCurrent();
            }
        });
        totalRentalsCash=0.0;
        totalRentalsTransfer=0.0;
        getRentals().forEach(rental -> {
            if(rental.isCash()){
                totalRentalsCash+=rental.getTotalCurrent();
            }else{
                totalRentalsTransfer+=rental.getTotalCurrent();
            }
        });
        totalReservesCash=0.0;
        totalReservesTransfer=0.0;
        getReserves().forEach(reserve ->{
            if(reserve.isCash()){
                totalReservesCash+=reserve.getTotal();
            }else{
                totalReservesTransfer+=reserve.getTotal();
            }
        });
        totalMovements=0.0;
        getMovements().forEach(movement -> {
            if(movement.isEntrance()){
                totalMovements+=movement.getAmount();
            }else{
                totalMovements-=movement.getAmount();
            }
        });
        amountToDelivered+=(totalSalesCash+totalRentalsCash+totalReservesCash+totalMovements);
        save();
    }
}
