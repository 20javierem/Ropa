package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "style_tbl")
public class Style extends Babas {

    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @NotBlank(message = "Nombre")
    @NotNull(message = "Nombre")
    private String name;
    @NotEmpty(message = "Presentaciones")
    @OneToMany(mappedBy = "style")
    private List<Presentation> presentations=new ArrayList<>();
    private Date created=new Date();
    private Date updated;
    @ManyToOne
    @NotNull(message = "Categoría")
    private Category category;
    @ManyToOne
    @NotNull(message = "Marca")
    private Brand brand;
    @OneToMany(mappedBy = "style")
    @Where(clause = "active==true")
    private List<Product> products=new ArrayList<>();
    @Transient
    private Presentation presentationDefault;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Presentation> getPresentations() {
        return presentations;
    }

    public void setPresentations(List<Presentation> presentations) {
        this.presentations = presentations;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Product> getProducts() {
        return products;
    }


    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public static class ListCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Style) {
                value = ((Style) value).getName();
            }
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            return this;
        }
    }
    public void setPresentationDefault(Presentation presentationDefault){
        this.presentationDefault=presentationDefault;
    }
    public Presentation getPresentationDefault() {
        if(presentationDefault==null){
            for (Presentation presentation : getPresentations()) {
                if(presentation.isDefault()){
                    presentationDefault=presentation;
                    return presentationDefault;
                }
            }
        }
        return presentationDefault;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void save() {
        updated=new Date();
        super.save();
        getPresentations().forEach(Presentation::save);
    }
}


