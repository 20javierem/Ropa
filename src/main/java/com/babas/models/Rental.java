package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "rental_tbl")
public class Rental extends Babas {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @ManyToOne
    @NotNull(message = "Caja")
    private BoxSession boxSession;
    @OneToMany(mappedBy = "rental")
    @NotEmpty(message = "Productos")
    private List<DetailRental> detailRentals=new ArrayList<>();
    @ManyToOne
    @NotNull(message = "Cliente")
    private Client client;
    private Date created;
    private Date updated;
    private Date delivery;
    @NotNull(message = "Fecha fin")
    private Date ended;
    private Long numberRental;
    @ManyToOne
    private Branch branch;
    private boolean active=true;
    private boolean cash;
    private Double total=0.0;
    private Double discount=0.0;
    private Double totalCurrent=0.0;
    private Double totalCurrentWithPenalty=0.0;
    private Double warranty=0.0;
    private Double penalty=0.0;
    @ManyToOne
    @NotNull(message ="Usuario")
    private User user;

    public Long getId() {
        return id;
    }

    public BoxSession getBoxSession() {
        return boxSession;
    }

    public void setBoxSession(BoxSession boxSession) {
        this.boxSession = boxSession;
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

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isCash() {
        return cash;
    }

    public void setCash(boolean cash) {
        this.cash = cash;
    }

    public Long getNumberRental() {
        return numberRental;
    }

    public Double getTotal() {
        return total;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTotalCurrent() {
        return totalCurrent;
    }

    public List<DetailRental> getDetailRentals() {
        return detailRentals;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void calculateTotal(){
        total=0.0;
        detailRentals.forEach(detailSale -> {
            total+=detailSale.getSubtotal();
        });
        totalCurrent=total+warranty-discount;
        totalCurrentWithPenalty=totalCurrent+penalty;
    }

    public Double getTotalCurrentWithPenalty() {
        return totalCurrentWithPenalty;
    }

    public Double getWarranty() {
        return warranty;
    }

    public void setWarranty(Double warranty) {
        this.warranty = warranty;
    }

    public Date getEnded() {
        return ended;
    }

    public void setEnded(Date ended) {
        this.ended = ended;
    }

    public Double getPenalty() {
        return penalty;
    }

    public void setPenalty(Double penalty) {
        this.penalty = penalty;
    }

    public Date getDelivery() {
        return delivery;
    }

    public void setDelivery(Date delivery) {
        this.delivery = delivery;
    }

    @Override
    public void save() {
        if(created==null){
            created=new Date();
        }
        updated=new Date();
        super.save();
        numberRental=1000+id;
        super.save();
        getDetailRentals().forEach(Babas::save);
    }
}
