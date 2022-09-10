package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "sale_tbl")
public class Sale extends Babas {

    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @OneToMany(mappedBy = "sale")
    private List<DetailSale> detailSales=new ArrayList<>();
    @ManyToOne
    @NotNull(message = "Usuario")
    private User user;
    @ManyToOne
    private Client client;
    private Double total=0.0;
    private Double discount=0.0;
    private Double totalCurrent=0.0;
    private Date created=new Date();
    private Date updated;

    public Long getId() {
        return id;
    }

    public List<DetailSale> getDetailSales() {
        return detailSales;
    }

    public void setDetailSales(List<DetailSale> detailSales) {
        this.detailSales = detailSales;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
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

    public void setTotalCurrent(Double totalCurrent) {
        this.totalCurrent = totalCurrent;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
