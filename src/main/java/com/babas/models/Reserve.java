package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "reserve_tbl")
public class Reserve extends Babas {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @ManyToOne
    private Client client;
    @ManyToOne
    @NotNull(message = "Caja")
    private BoxSession boxSession;
    @ManyToOne
    @NotNull
    private Branch branch;
    private Date created;
    private Date updated;
    @NotNull(message = "Fecha de inicio")
    private Double started;
    @NotNull(message = "Fecha de finalización")
    private Double ended;
    @OneToMany(mappedBy = "reserve")
    @NotEmpty(message = "Productos")
    private List<DetailReserve> detailReserves=new ArrayList<>();
    private Double total=0.0;
    private Double advance=0.0;
    private Double toCancel=0.0;

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

    public void setTotal(Double total) {
        this.total = total;
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

    public void setToCancel(Double toCancel) {
        this.toCancel = toCancel;
    }

    public Double getStarted() {
        return started;
    }

    public void setStarted(Double started) {
        this.started = started;
    }

    public Double getEnded() {
        return ended;
    }

    public void setEnded(Double ended) {
        this.ended = ended;
    }

    @Override
    public void save() {
        if(created==null){
            created=new Date();
        }
        updated=new Date();
        super.save();
    }
}
