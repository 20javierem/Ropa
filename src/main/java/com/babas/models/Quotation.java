package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "quotation_tbl")
public class Quotation extends Babas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "quotation")
    @NotEmpty(message = "Productos")
    private List<DetailQuotation> detailQuotations=new ArrayList<>();
    @NotNull(message = "Válido hasta")
    private Date ended;
    private Date created;
    @ManyToOne
    @NotNull(message = "Sucursal")
    private Branch branch;
    @ManyToOne
    private Client client;
    private Double total=0.0;
    private Double discount=0.0;
    private Double totalCurrent=0.0;
    @ManyToOne
    @NotNull(message ="Usuario")
    private User user;
    private String observation;
    private Long numberQuotation;

    public void calculateTotals(){
        total=0.0;
        detailQuotations.forEach(detailSale -> {
            total+=detailSale.getSubtotal();
        });
        totalCurrent=total-discount;

    }

    public Long getId() {
        return id;
    }

    public List<DetailQuotation> getDetailQuotations() {
        return detailQuotations;
    }

    public void setDetailQuotations(List<DetailQuotation> detailQuotations) {
        this.detailQuotations = detailQuotations;
    }

    public Date getEnded() {
        return ended;
    }

    public void setEnded(Date ended) {
        this.ended = ended;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Long getNumberQuotation() {
        return numberQuotation;
    }

    @Override
    public void save() {
        if(created==null){
            created=new Date();
        }
        super.save();
        numberQuotation=1000+id;
        super.save();
        getDetailQuotations().forEach(Babas::save);
    }
}
