package com.babas.models;

import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

@Entity(name = "product_tbl")
public class Product extends Babas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @OneToMany(mappedBy = "product")
    @Where(clause = "active=1")
    private List<Stock> stocks=new ArrayList<>();
    private Date created;
    private Date updated;
    private Integer stockTotal=0;
    private boolean active=true;
    private String barcode;
    private String uniqueCode;
    @ManyToOne
    private Stade stade;
    @ManyToOne
    private Dimention dimention;
    @ManyToOne
    private Brand brand;
    @OneToMany(mappedBy = "product")
    @Where(clause = "active=1")
    private List<Presentation> presentations=new ArrayList<>();
    private Integer numberImage=1;
    @Transient
    private Presentation presentationDefault;

    @ElementCollection
    @CollectionTable(name="Imagesx200", joinColumns=@JoinColumn(name="product_id"))
    @Column(name="image")
    private List<String> imagesx200=new ArrayList<>();
    @Transient
    private List<Icon> iconsx200=new ArrayList<>();

    @ElementCollection
    @CollectionTable(name="Imagesx400", joinColumns=@JoinColumn(name="product_id"))
    @Column(name="image")
    private List<String> imagesx400=new ArrayList<>();
    @Transient
    private List<Icon> iconsx400=new ArrayList<>();

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

    public void calculateStockTotal(){
        stockTotal=0;
        stocks.forEach(stock ->{
            stockTotal+=stock.getQuantity();
        });
    }
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getImagesx200() {
        return imagesx200;
    }

    public void setImagesx200(List<String> imagesx200) {
        this.imagesx200 = imagesx200;
    }

    public void setIconsx200(List<Icon> iconsx200) {
        this.iconsx200 = iconsx200;
    }

    public List<String> getImagesx400() {
        return imagesx400;
    }

    public void setImagesx400(List<String> imagesx400) {
        this.imagesx400 = imagesx400;
    }

    public void setIconsx400(List<Icon> iconsx400) {
        this.iconsx400 = iconsx400;
    }

    public String getBarcode() {
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

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public List<Icon> getIconsx200(){
        return iconsx200;
    }

    public void loadImages(){
        iconsx200.clear();
        iconsx400.clear();
        imagesx200.forEach(icon->{
            Image image=Utilities.getImage(icon,false);
            if(image!=null){
                iconsx200.add(new ImageIcon(image));
            }
        });
        imagesx400.forEach(icon->{
            Image image=Utilities.getImage(icon,false);
            if(image!=null){
                iconsx400.add(new ImageIcon(image));
            }
        });
    }
    public List<Icon> getIconsx400(){
        return iconsx400;
    }

    public Integer getNumberImage() {
        return numberImage;
    }

    public void setNumberImage(Integer numberImage) {
        this.numberImage = numberImage;
    }

    @Override
    public void save() {
        if(created==null){
            created=new Date();
        }
        updated=new Date();
        super.save();
        if(barcode.isBlank()){
            barcode= String.valueOf(1000+getId());
            super.save();
        }
        if(uniqueCode==null){
            uniqueCode=Utilities.formatUniqueCode.format(new Date())+"-"+id;
            super.save();
        }
        getPresentations().forEach(Presentation::save);
    }

    public void setStockTotal(Integer stockTotal) {
        this.stockTotal=stockTotal;
    }
}
