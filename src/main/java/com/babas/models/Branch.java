package com.babas.models;

import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "branch_tbl")
public class Branch extends Babas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Nombre")
    private String name;
    private String email;
    @NotBlank(message = "Dirección")
    private String direction;
    @NotBlank(message = "Celular")
    private String phone;
    private String message="";
    @ManyToOne
    private Company company;
    @NotNull
    private boolean active=true;
    @ManyToMany(mappedBy = "branchs")
    private List<User> users =new ArrayList<>();
    @OneToMany(mappedBy = "source")
    @Where(clause = "state=0")
    private List<Transfer> transfers_sources =new ArrayList<>();
    @OneToMany(mappedBy = "destiny")
    @Where(clause = "state=0")
    private List<Transfer> transfers_destinys =new ArrayList<>();
    @Transient
    private List<Transfer> transfers=new ArrayList<>();
    @OneToMany(mappedBy = "branch")
    @Where(clause = "p1_0.active=1")
    private List<Stock> stocks =new ArrayList<>();
    private Date created;
    private Date updated;
    @OneToMany
    private List<Sale> sales=new ArrayList<>();
    @OneToMany(mappedBy = "branch")
    private List<Box> boxs=new ArrayList<>();
    private Integer idFact=0;
    @NotBlank(message = "Ubigeo")
    private String ubigeo;
    private String serieNotaVenta="N001";
    private String serieBoleta="B001";
    private String serieFactura="F001";
    private Long correlativoNotaVenta=0L;
    private Long correlativoBoleta=0L;
    private Long correlativoFactura=0L;



    public Branch(){

    }

    public String getSerieNotaVenta() {
        return serieNotaVenta;
    }

    public void setSerieNotaVenta(String serieNotaVenta) {
        this.serieNotaVenta = serieNotaVenta;
    }

    public Long getCorrelativoNotaVenta() {
        return correlativoNotaVenta;
    }

    public void setCorrelativoNotaVenta(Long correlativoNotaVenta) {
        this.correlativoNotaVenta = correlativoNotaVenta;
    }

    public String getSerieBoleta() {
        return serieBoleta;
    }

    public void setSerieBoleta(String serieBoleta) {
        this.serieBoleta = serieBoleta;
    }

    public Long getCorrelativoBoleta() {
        return correlativoBoleta;
    }

    public void setCorrelativoBoleta(Long correlativoBoleta) {
        this.correlativoBoleta = correlativoBoleta;
    }

    public String getSerieFactura() {
        return serieFactura;
    }

    public void setSerieFactura(String serieFactura) {
        this.serieFactura = serieFactura;
    }

    public Long getCorrelativoFactura() {
        return correlativoFactura;
    }

    public void setCorrelativoFactura(Long correlativoFactura) {
        this.correlativoFactura = correlativoFactura;
    }

    public Integer getIdFact() {
        return idFact;
    }

    public void setIdFact(Integer idFact) {
        this.idFact = idFact;
    }

    public Branch(String name){
        this.name=name;
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public List<User> getUsers() {
        return users;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public List<Transfer> getTransfers_sources() {
        return transfers_sources;
    }

    public List<Transfer> getTransfers_destinys() {
        return transfers_destinys;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = Utilities.nameOwn(ubigeo);
    }

    public static class ListCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Branch) {
                value = ((Branch) value).getName();
            }else{
                value="--SELECCIONE--";
            }
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            return this;
        }
    }

    public List<Transfer> getTransfers() {
        if(transfers.size()!=(transfers_destinys.size()+transfers_sources.size())){
            transfers.clear();
            transfers.addAll(transfers_sources);
            transfers_destinys.forEach(transfer -> {
                if(!transfers.contains(transfer)){
                    transfers.add(transfer);
                }
            });
        }
        return transfers;
    }

    public List<Box> getBoxs() {
        return boxs;
    }

    @Override
    public void save() {
        if(created==null){
            created=new Date();
        }
        updated=new Date();
        super.save();
        getBoxs().forEach(Box::save);
    }
}
