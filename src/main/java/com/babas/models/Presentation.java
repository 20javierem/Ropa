package com.babas.models;

import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "presentation_tbl")
public class Presentation extends Babas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne
    private Product product;
    @NotBlank(message = "Nombre")
    private String name;
    @NotNull
    private Integer quantity;
    @OneToMany(mappedBy = "presentation")
    @Where(clause = "active=1")
    private List<Price> prices =new ArrayList<>();
    private Date created=new Date();
    private Date updated;
    private boolean isDefault=false;
    private boolean active=true;
    @Transient
    private Price priceDefault;
    private String uniqueCode;

    public Presentation(Product product){
        this.product=product;
    }

    public Presentation() {

    }
    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public void  setPriceDefault(Price priceDefault){
        this.priceDefault=priceDefault;
    }

    public static class ListCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Presentation) {
                value = ((Presentation) value).getName();
            }
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            return this;
        }
    }

    public Price getPriceDefault() {
        if(priceDefault==null){
            for (Price price : getPrices()) {
                if(price.isDefault()){
                    priceDefault=price;
                    return priceDefault;
                }
            }
        }
        return priceDefault;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void save() {
        updated=new Date();
        super.save();
        if(uniqueCode==null){
            uniqueCode=Utilities.formatUniqueCode.format(new Date())+"-"+id;
            super.save();
        }
        getPrices().forEach(Price::save);
    }

    @Override
    public String toString() {
        return name;
    }
}
