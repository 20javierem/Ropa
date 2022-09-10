package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "transfer_tbl")
public class Transfer extends Babas {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @NotNull(message = "Origen")
    @ManyToOne
    private Branch source;
    @NotNull(message = "Destino")
    @ManyToOne
    private Branch destiny;
    @NotEmpty(message = "Productos")
    @OneToMany(mappedBy = "transfer")
    private List<DetailTransfer> detailTransfers=new ArrayList<>();
    private Integer state=0;
    private Integer productsTransfers=0;
    private Double total=0.0;
    private String description="";
    private Date created=new Date();
    private Date updated;

    public Long getId() {
        return id;
    }

    public List<DetailTransfer> getDetailTransfers() {
        return detailTransfers;
    }

    public void setDetailTransfers(List<DetailTransfer> detailTransfers) {
        this.detailTransfers = detailTransfers;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getProductsTransfers() {
        return productsTransfers;
    }

    public void setProductsTransfers(Integer productsTransfers) {
        this.productsTransfers = productsTransfers;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Branch getSource() {
        return source;
    }

    public void setSource(Branch source) {
        this.source = source;
    }

    public Branch getDestiny() {
        return destiny;
    }

    public void setDestiny(Branch destiny) {
        this.destiny = destiny;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}


