package com.babas.models;

import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity(name = "detailQuotation_tbl")
public class DetailQuotation extends Babas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Quotation quotation;
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

    public Quotation getQuotation() {
        return quotation;
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
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
        this.subtotal=Math.round(quantity*price*100.0)/100.0;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
        this.subtotal=Math.round(quantity*price*100.0)/100.0;
    }

    public Integer getQuantityPresentation() {
        return quantityPresentation;
    }

    public void setQuantityPresentation(Integer quantityPresentation) {
        this.quantityPresentation = quantityPresentation;
    }

    public String getNamePresentation() {
        return namePresentation;
    }

    public void setNamePresentation(String namePresentation) {
        this.namePresentation = namePresentation;
    }

    public String getNameUnity(){
        return namePresentation;
    }
    public String getCodeProduct(){
        return product.getBarcode();
    }
    public String getProductString(){
        return product.getStyle().getName()+" - "+product.getBarcode();
    }
    public String getSub_totalString(){
        return Utilities.moneda.format(getSubtotal());
    }
    public String getPriceString(){
        return Utilities.moneda.format(getPrice());
    }
}
