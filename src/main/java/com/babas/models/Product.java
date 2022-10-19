package com.babas.models;

import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

@Entity(name = "product_tbl")
public class Product extends Babas {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @ManyToOne
    @NotNull(message = "Estilo")
    private Style style;
    @ManyToOne
    @NotNull(message = "Color")
    private Color color;
    @ManyToOne
    @NotNull(message = "Género")
    private Sex sex;
    @ManyToOne
    @NotNull(message = "Talla")
    private Size size;
    @ElementCollection
    @CollectionTable(name="Images", joinColumns=@JoinColumn(name="product_id"))
    @Column(name="image")
    private List<String> images=new ArrayList<>();
    @OneToMany(mappedBy = "product")
    private List<Stock> stocks=new ArrayList<>();
    private Date created;
    private Date updated;
    private Integer stockTotal=0;
    private boolean active=true;
    private Long barcode;
    @ManyToOne
    private Stade stade;
    @ManyToOne
    private Dimention dimention;
    @ManyToOne
    private Brand brand;
    @NotEmpty(message = "Presentaciones")
    @OneToMany(mappedBy = "product")
    private List<Presentation> presentations=new ArrayList<>();
    @Transient
    private Presentation presentationDefault;
    @Transient
    private List<Icon> iconsx200=new ArrayList<>();
    @Transient
    private List<Icon> iconsx800=new ArrayList<>();

    public Long getId() {
        return id;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Integer getStockTotal() {
        return stockTotal;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getImages() {
        return images;
    }

    public Long getBarcode() {
        return barcode;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public Stade getStade() {
        return stade;
    }

    public void setStade(Stade stade) {
        this.stade = stade;
    }

    public Dimention getDimention() {
        return dimention;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public void setDimention(Dimention dimention) {
        this.dimention = dimention;
    }

    public List<Presentation> getPresentations() {
        return presentations;
    }

    public void setPresentationDefault(Presentation presentationDefault){
        this.presentationDefault=presentationDefault;
    }

    public static class ListCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Product) {
                Product product=(Product) value;
                value = product.getBrand().getName()+" / "+product.getSex().getName()+" / "+product.getSize().getName()+" / "+product.getColor().getName();
                if(product.getStade()!=null){
                   value+=" / "+product.getStade().getName();
                }
                if(product.getDimention()!=null){
                    value+=" / "+product.getDimention().getName();
                }
            }
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            return this;
        }
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
    public List<Icon> getIconsx200(){
        if(iconsx200.size()==images.size()){
            return  iconsx200;
        }else{
            iconsx200.clear();
            images.forEach(icon->{
                Image image=Utilities.getImage(icon);
                if(image!=null){
                    int width=image.getWidth(null);
                    int height=image.getHeight(null);
                    if(width>200||height>200){
                        double percen= Math.min(200.00/width,200.00/height);
                        width= (int) (percen*width);
                        height=(int) (percen*height);
                    }
                    image=image.getScaledInstance(width, height,  Image.SCALE_FAST);
                    iconsx200.add(new ImageIcon(image));
                }

            });
            return iconsx200;
        }
    }
    public List<Icon> getIconsx800(){
        if(iconsx800.size()==images.size()){
            return  iconsx800;
        }else{
            iconsx800.clear();
            images.forEach(icon->{
                Image image=Utilities.getImage(icon);
                if(image!=null){
                    int width=image.getWidth(null);
                    int height=image.getHeight(null);
                    if(width>800||height>800){
                        double percen= Math.min(800.00/width,800.00/height);
                        width= (int) (percen*width);
                        height=(int) (percen*height);
                    }
                    image=image.getScaledInstance(width, height,  Image.SCALE_SMOOTH);
                    iconsx800.add(new ImageIcon(image));
                }

            });
            return iconsx800;
        }
    }
    @Override
    public void save() {
        if(created==null){
            created=new Date();
        }
        updated=new Date();
        super.save();
        if(barcode==null){
            barcode=1000+getId();
            super.save();
        }
        getPresentations().forEach(Presentation::save);
    }

    public void setStockTotal(Integer stockTotal) {
        this.stockTotal=stockTotal;
    }
}
