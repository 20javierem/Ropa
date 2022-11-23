package com.babas.models;

import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "reserve_tbl")
public class Reserve extends Babas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @NotNull(message = "Cliente")
    private Client client;
    @ManyToOne
    @NotNull(message = "Usuario")
    private User user;
    @ManyToOne
    @NotNull(message = "Caja")
    private BoxSession boxSession;
    @ManyToOne
    @NotNull
    private Branch branch;
    private Date created;
    private Date updated;
    @NotNull(message = "Fecha de entrega")
    private Date started;
    @OneToMany(mappedBy = "reserve")
    @NotEmpty(message = "Productos")
    private List<DetailReserve> detailReserves=new ArrayList<>();
    private Double total=0.0;
    private Double advance=0.0;
    private Double toCancel=0.0;
    private Long numberReserve;
    private boolean active=true;
    private boolean cash;
    private String observation;
    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isCash() {
        return cash;
    }

    public void setCash(boolean cash) {
        this.cash = cash;
    }

    public Long getId() {
        return id;
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

    public List<DetailReserve> getDetailReserves() {
        return detailReserves;
    }

    public Double getTotal() {
        return total;
    }

    public BoxSession getBoxSession() {
        return boxSession;
    }

    public void setBoxSession(BoxSession boxSession) {
        this.boxSession = boxSession;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Double getAdvance() {
        return advance;
    }

    public void setAdvance(Double advance) {
        this.advance = advance;
    }

    public Double getToCancel() {
        return toCancel;
    }
    public void calculateTotal(){
        total=0.0;
        detailReserves.forEach(detailSale -> {
            total+=detailSale.getSubtotal();
        });
        toCancel=total-advance;
    }

    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getNumberReserve() {
        return numberReserve;
    }

    public String getStringUpdated(){
        return Utilities.formatoFechaHora.format(updated);
    }
    public String getStringBranch(){
        return branch.getName();
    }
    public String getStringClient(){
        return client==null?"--":client.getNames();
    }
    public String getStringStade(){
        return active?"Realizado":"Cancelado";
    }
    public String getStringSubtotal(){
        return Utilities.moneda.format(total);
    }
    public String getStringType(){
        return cash?"Efectivo":"Transferencia";
    }
    public String getStringAdvance(){return Utilities.moneda.format(advance);}
    public String getStringToCancel(){return Utilities.moneda.format(toCancel);}

    @Override
    public void save() {
        if(created==null){
            created=new Date();
        }
        updated=new Date();
        super.save();
        numberReserve=1000+id;
        super.save();
        getDetailReserves().forEach(Babas::save);
    }
}
