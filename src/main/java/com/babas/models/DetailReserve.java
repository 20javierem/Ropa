package com.babas.models;

import com.babas.controllers.Stocks;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "detail_reserve_tbl")
public class DetailReserve extends Babas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Reserve reserve;
    @ManyToOne
    @NotNull(message = "Producto")
    private Product product;
    @ManyToOne
    private Presentation presentation;
    @Min(value = 1,message = "Cantidad")
    private Integer quantity=0;
    private Double subtotal=0.0;
    private Double price=0.0;
    private Integer quantityPresentation=0;
    private String namePresentation;

    public Long getId() {
        return id;
    }

    public Reserve getReserve() {
        return reserve;
    }

    public void setReserve(Reserve reserve) {
        this.reserve = reserve;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Presentation getPresentation() {
        return presentation;
    }

    public void setPresentation(Presentation presentation) {
        this.presentation = presentation;
        if(presentation!=null){
            quantityPresentation=presentation.getQuantity();
            namePresentation=presentation.getName();
        }
    }

    public String getNamePresentation() {
        return namePresentation;
    }

    public void setNamePresentation(String namePresentation) {
        this.namePresentation = namePresentation;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        this.subtotal= quantity*price;
    }
    public String getNameUnity(){
        return namePresentation;
    }
    public String getCodeProduct(){
        return product.getBarcode();
    }
    public String getProductString(){
        return product.getStyle().getName();
    }
    public String getSub_totalString(){
        return Utilities.moneda.format(getSubtotal());
    }
    public String getPriceString(){
        return Utilities.moneda.format(getPrice());
    }
    public Double getSubtotal() {
        return subtotal;
    }

    public Integer getQuantityPresentation() {
        return quantityPresentation;
    }

    public void setQuantityPresentation(Integer quantityPresentation) {
        this.quantityPresentation = quantityPresentation;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
        this.subtotal= quantity*price ;
    }

    @Override
    public void save() {
        Stock stock= Stocks.getStock(getReserve().getBranch(),getProduct());
        stock.refresh();
        if(getReserve().isActive()==0){
            stock.setOnReserve(stock.getOnReserve()+getQuantity()*getQuantityPresentation());
        }else{
            stock.setOnReserve(stock.getOnReserve()-getQuantity()*getQuantityPresentation());
        }
        stock.save();
        super.save();
    }
}
