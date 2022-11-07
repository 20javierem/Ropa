package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "detailTransfer_tbl")
public class DetailTransfer extends Babas {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "identity")
    private Long id;
    @ManyToOne
    private Transfer transfer;
    @ManyToOne
    @NotNull(message = "Producto")
    private Product product;
    private Integer quantity=0;

    public Long getId() {
        return id;
    }

    public Transfer getTransfer() {
        return transfer;
    }

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStringCode(){
        return String.valueOf(product.getBarcode());
    }
    public String getStringProduct(){
        return product.getStyle().getName();
    }
    public String getStringColor(){
        return product.getColor().getName();
    }
    public String getStringSize(){
        return product.getSize().getName();
    }
}
