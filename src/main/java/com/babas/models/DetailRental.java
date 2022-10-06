package com.babas.models;

import com.babas.controllers.Stocks;
import com.babas.utilities.Babas;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity(name = "detailRental_tbl")
public class DetailRental extends Babas {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @ManyToOne
    private Rental rental;
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

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
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
        Stock stock= Stocks.getStock(getRental().getBranch(),getProduct());
        stock.refresh();
        stock.getProduct().refresh();
        if (getRental().isActive()){
            stock.setQuantity(stock.getQuantity()-getQuantity()*getQuantityPresentation());
            stock.addOnRental();
            stock.save();
        }else{
            stock.setQuantity(stock.getQuantity()+getQuantity()*getQuantityPresentation());
            stock.removeOnRental();
            stock.save();
        }
    }
}
