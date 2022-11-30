package com.babas.models;

import com.babas.controllers.Stocks;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "detailRental_tbl")
public class DetailRental extends Babas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Rental rental;
    @ManyToOne
    @NotNull(message = "Producto")
    private Product product;
    @Transient
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

    public void setNamePresentation(String namePresentation) {
        this.namePresentation = namePresentation;
    }

    public void setQuantityPresentation(Integer quantityPresentation) {
        this.quantityPresentation = quantityPresentation;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        this.subtotal=Math.round(quantity*price*100.0)/100.0;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
        this.subtotal=Math.round(quantity*price*100.0)/100.0;
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
        return getSubtotal().toString();
    }
    public String getPriceString(){
        return getPrice().toString();
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
        if (getRental().isActive()==0){
            stock.setOnStock(stock.getOnStock()-getQuantity()*getQuantityPresentation());
            stock.setOnRental(stock.getOnRental()+getQuantity()*getQuantityPresentation());
            stock.setTimesRented(stock.getTimesRented()+getQuantity());
            stock.save();
        } else if(getRental().isActive()==1){
            stock.setOnStock(stock.getOnStock()+getQuantity()*getQuantityPresentation());
            stock.setOnRental(stock.getOnRental()-getQuantity()*getQuantityPresentation());
            stock.save();
        } else {
            stock.setOnStock(stock.getOnStock()+getQuantity()*getQuantityPresentation());
            stock.setOnRental(stock.getOnRental()-getQuantity()*getQuantityPresentation());
            stock.setTimesRented(stock.getTimesRented()-getQuantity());
            stock.save();

        }
    }
}
