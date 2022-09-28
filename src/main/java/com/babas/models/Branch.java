package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Where;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "branch_tbl")
public class Branch extends Babas {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @NotBlank(message = "Nombre")
    private String name;
    private String email;
    @NotBlank(message = "Dirección")
    private String direction;
    @NotBlank(message = "Celular")
    private String phone;
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
    private List<Stock> stocks =new ArrayList<>();
    private Date created;
    private Date updated;
    @OneToMany
    private List<Sale> sales=new ArrayList<>();
    @OneToMany(mappedBy = "branch")
    private List<Box> boxs=new ArrayList<>();

    public Branch(){

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
        this.name = name;
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
    }
}
