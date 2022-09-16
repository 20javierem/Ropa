package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity(name = "reserve_tbl")
public class Reserve extends Babas {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @ManyToOne
    private Client client;
    private Date created=new Date();
    private Date ends;
    private Date updated;
    @OneToMany(mappedBy = "reserve")
    @NotEmpty(message = "Productos")
    private List<DetailReserve> detailReserves=new ArrayList<>();
    private Double total=0.0;

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

    public Date getEnds() {
        return ends;
    }

    public void setEnds(Date ends) {
        this.ends = ends;
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


}
