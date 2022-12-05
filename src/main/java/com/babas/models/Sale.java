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
import java.util.Objects;

@Entity(name = "sale_tbl")
public class Sale extends Babas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "sale")
    @NotEmpty(message = "Productos")
    private List<DetailSale> detailSales = new ArrayList<>();
    @ManyToOne
    @NotNull(message = "Usuario")
    private User user;
    @ManyToOne
    private Client client;
    @ManyToOne
    @NotNull(message = "Caja")
    private BoxSession boxSession;
    private Double total = 0.0;
    private Double discount = 0.0;
    private Double totalCurrent = 0.0;
    private boolean cash;
    private Date created;
    private Date updated;
    @ManyToOne
    @NotNull
    private Branch branch;
    @OneToOne
    private Reserve reserve;
    private String observation;
    private String serie;
    private Long correlativo;
    private String typeVoucher;
    private boolean statusSunat = false;
    private boolean active = true;

    public Sale() {

    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

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

    public boolean isStatusSunat() {
        return statusSunat;
    }

    public void setStatusSunat(boolean statusSunat) {
        this.statusSunat = statusSunat;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getTypeVoucher() {
        return typeVoucher;
    }

    public String getTypeStringVoucher(){
        switch (getTypeVoucher()) {
            case "77":
                return "NOTA";
            case "03":
                return "BOLETA";
            default:
                return "FACTURA";
        }
    }
    public void setTypeVoucher(String typeVoucher) {
        this.typeVoucher = typeVoucher;
    }

    public Double getTotalCurrent() {
        return totalCurrent;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public Long getCorrelativo() {
        return correlativo;
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
            total=totalCurrent-reserve.getAdvance();
        }
        total=Math.round(total*100.0)/100.0;
        totalCurrent=Math.round(totalCurrent*100.0)/100.0;
    }
    public boolean isValidClient(String typeDocument){
        if(client!=null){
            if ("01".equals(typeDocument)) {
                return client.getDni().length() == 11;
            }
            return true;
        }else{
            return typeDocument.equals("77")||typeDocument.equals("03");
        }
    }
    public String getStringUpdated(){
        return Utilities.formatoFechaHora.format(updated);
    }
    public String getStringBranch(){
        return branch.getName();
    }
    public String getStringStade(){
        return active?"REALIZADO":"CANCELADO";
    }
    public String getStringSunat(){
        return statusSunat?"CONFIRMADO":"PENDIENTE";
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
    public String getStringClient(){
        return client!=null?client.getNames():"";
    }
    public String getClientDni( ){
        return client!=null?client.getDni():"00000000";
    }
    public int getClientType(){
        return client!=null?client.getTypeDocument():1;
    }
    public String getClientAdress(){
        return client!=null?client.getMail():"";
    }
    public String getStringTypeDocument() {
        switch (getTypeVoucher()) {
            case "77":
                return "NOTA DE VENTA ELECTRÓNICA";
            case "03":
                return "BOLETA DE VENTA ELECTRÓNICA";
            default:
                return "FACTURA DE VENTA ELECTRÓNICA";
        }
    }
    public String getDetailTicket(){
        switch (getTypeVoucher()) {
            case "77":
                return "Representacion Impresa de la Nota de Venta Electrónica";
            case "03":
                return "Representacion Impresa de la Boleta de Venta Electrónica";
            default:
                return "Representacion Impresa de la Factura de Venta Electrónica";
        }
    }
    public String getContentQR(){
        return Babas.company.getRuc()+"|"+typeVoucher+"|"+getSerie()+"|"+getCorrelativo()+"|0.0|"+getTotalCurrent()+"|"+Utilities.formatoFecha.format(new Date())+"|"+getClientType()+"|"+getClientDni();
    }
    public void create(){
        created=new Date();
        branch.refresh();
        switch (typeVoucher){
            case "01":
                branch.setCorrelativoFactura(branch.getCorrelativoFactura()+1);
                correlativo=branch.getCorrelativoFactura();
                serie=branch.getSerieFactura();
                break;
            case "03":
                branch.setCorrelativoBoleta(branch.getCorrelativoBoleta()+1);
                correlativo=branch.getCorrelativoBoleta();
                serie=branch.getSerieBoleta();
                break;
            default:
                branch.setCorrelativoNotaVenta(branch.getCorrelativoNotaVenta()+1);
                correlativo=branch.getCorrelativoNotaVenta();
                serie=branch.getSerieNotaVenta();
                break;
        }
        branch.save();
        save();
    }

    public void updateStocks(){
        getDetailSales().forEach(Babas::save);
    }

    @Override
    public void save() {
        updated=new Date();
        super.save();
    }

}
