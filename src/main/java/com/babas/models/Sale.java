package com.babas.models;

import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "Productos")
    private List<DetailSale> detailSales=new ArrayList<>();
    @ManyToOne
    @NotNull(message = "Usuario")
    private User user;
    @ManyToOne
    private Client client;
    @ManyToOne
    @NotNull(message = "Caja")
    private BoxSession boxSession;
    private Double total=0.0;
    private Double discount=0.0;
    private Double totalCurrent=0.0;
    private boolean cash;
    private Date created;
    private Date updated;
    private Long numberSale;
    private boolean active=true;
    @ManyToOne
    @NotNull
    private Branch branch;
    @OneToOne
    private Reserve reserve;

    public Long getId() {
        return id;
    }

    public List<DetailSale> getDetailSales() {
        return detailSales;
    }

    public Double getTotal() {
        return total;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
        calculateTotal();
    }

    public Double getTotalCurrent() {
        return totalCurrent;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BoxSession getBoxSession() {
        return boxSession;
    }

    public void setBoxSession(BoxSession boxSession) {
        this.boxSession = boxSession;
    }

    public boolean isCash() {
        return cash;
    }

    public void setCash(boolean cash) {
        this.cash = cash;
    }

    public Long getNumberSale() {
        return numberSale;
    }

    public Reserve getReserve() {
        return reserve;
    }

    public void setReserve(Reserve reserve) {
        this.reserve = reserve;
    }

    public void calculateTotal(){
        total=0.0;
        detailSales.forEach(detailSale -> {
            total+=detailSale.getSubtotal();
        });
        totalCurrent=total-discount;
        if(reserve!=null){
            total=totalCurrent- reserve.getAdvance();
        }
    }
    public String getStringUpdated(){
        return Utilities.formatoFecha.format(updated);
    }
    public String getStringBranch(){
        return branch.getName();
    }
    public String getStringClient(){
        return client==null?"--":client.getNames();
    }
    public String getStringStade(){
        return active?"REALIZADO":"CANCELADO";
    }
    public String getStringSubtotal(){
        return Utilities.moneda.format(total);
    }
    public String getStringDiscount(){
        return Utilities.moneda.format(discount);
    }
    public String getStringTotal(){
        return Utilities.moneda.format(totalCurrent);
    }
    public String getStringType(){
        return cash?"EFECTIVO":"TRANSFERENCIA";
    }
    @Override
    public void save() {
        if(created==null){
            created=new Date();
        }
        updated=new Date();
        super.save();
        numberSale=1000+id;
        super.save();
        getDetailSales().forEach(Babas::save);
    }
}
