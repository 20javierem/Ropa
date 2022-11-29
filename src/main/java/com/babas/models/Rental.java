package com.babas.models;

import com.babas.controllers.Rentals;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.views.frames.FPrincipal;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "rental_tbl")
public class Rental extends Babas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Integer active=0;
    private boolean cash;
    private Double total=0.0;
    private Double discount=0.0;
    private Double totalWithDiscount=0.0;
    private Double totalCurrent=0.0;
    private Double warranty=0.0;
    private Double penalty=0.0;
    @OneToOne
    private Reserve reserve;
    @ManyToOne
    @NotNull(message ="Usuario")
    private User user;
    private String observation;
    private String urlTicket;
    private String urlA4;
    private String cdr;

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
    public Rental(){

    }

    public Rental(Reserve reserve){
        this.reserve=reserve;
        for (DetailReserve detailReserve : reserve.getDetailReserves()) {
            DetailRental detailRental=new DetailRental();
            detailRental.setRental(this);
            detailRental.setPrice(detailReserve.getPrice());
            detailRental.setQuantity(detailReserve.getQuantity());
            detailRental.setPresentation(detailReserve.getPresentation());
            detailRental.setProduct(detailReserve.getProduct());
            detailRentals.add(detailRental);
        }
        calculateTotals();
    }
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

    public Integer isActive() {
        return active;
    }

    public void setActive(Integer active) {
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

    public void calculateTotals(){
        total=0.0;
        detailRentals.forEach(detailSale -> {
            total+=detailSale.getSubtotal();
        });
        totalWithDiscount=total-discount;
        totalCurrent=totalWithDiscount+warranty;
        if(reserve!=null){
            totalCurrent=totalCurrent-reserve.getAdvance();
        }
    }

    public Double getTotalWithDiscount() {
        return totalWithDiscount;
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

    public Reserve getReserve() {
        return reserve;
    }

    public void setReserve(Reserve reserve) {
        this.reserve = reserve;
    }

    public String getUrlTicket() {
        return urlTicket;
    }

    public void setUrlTicket(String urlTicket) {
        this.urlTicket = urlTicket;
    }

    public String getUrlA4() {
        return urlA4;
    }

    public void setUrlA4(String urlA4) {
        this.urlA4 = urlA4;
    }

    public String getCdr() {
        return cdr;
    }

    public void setCdr(String cdr) {
        this.cdr = cdr;
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
        return active==0?"EN ALQUILER":active==1?"COMPLETADA":"CANCELADA";
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
    public String getStringMulta(){
        return Utilities.moneda.format(getPenalty());
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
        if(active==0&&reserve!=null){
            reserve.setActive(1);
            reserve.save();
            FPrincipal.reservesActives.remove(reserve);
        }
        getDetailRentals().forEach(Babas::save);
    }
}
