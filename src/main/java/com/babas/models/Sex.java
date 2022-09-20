package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Sex extends Babas {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @NotBlank(message = "Nombre")
    private String name;
    private boolean active=true;
    private Date created=new Date();
    private Date updated;
    @OneToMany(mappedBy = "sex")
    private List<Product> products=new ArrayList<>();

    public Sex(){

    }

    public Sex(String name){
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

    public List<Product> getProducts() {
        return products;
    }

    public static class ListCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Sex) {
                value = ((Sex) value).getName();
            }else{
                value="--SELECCIONE--";
            }
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            return this;
        }
    }

    @Override
    public void save() {
        updated=new Date();
        super.save();
    }
}
