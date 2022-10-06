package com.babas.models;

import com.babas.controllers.Stocks;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity(name = "detailSale_tbl")
public class DetailSale extends Babas {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @ManyToOne
    private Sale sale;
    @ManyToOne
    @NotNull(message = "Producto")
    private Product product;
    @Transient
    @NotNull(message = "Producto")
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

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        this.subtotal= quantity*price;
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

    public String getNamePresentation() {
        return namePresentation;
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
        if(presentation!=null){
            namePresentation=presentation.getName();
        }
        super.save();
        Stock stock= Stocks.getStock(getSale().getBranch(),getProduct());
        stock.getProduct().refresh();
        if (getSale().isActive()){
            stock.getProduct().setStockTotal(stock.getProduct().getStockTotal()-getQuantity());
            stock.getProduct().save();
            stock.setQuantity(stock.getQuantity()-getQuantity()*getQuantityPresentation());
            stock.save();
        }else{
            stock.getProduct().setStockTotal(stock.getProduct().getStockTotal()+getQuantity());
            stock.getProduct().save();
            stock.setQuantity(stock.getQuantity()+getQuantity()*getQuantityPresentation());
            stock.save();
        }
    }
}
