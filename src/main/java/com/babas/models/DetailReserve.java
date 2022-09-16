package com.babas.models;

import com.babas.utilities.Babas;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity(name = "detail_reserve_tbl")
public class DetailReserve extends Babas {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @ManyToOne
    private Reserve reserve;
    @ManyToOne
    @NotNull(message = "Producto")
    private Product product;
    @Min(value = 1,message = "Cantidad")
    private Integer quantity;
    private Double total=0.0;



}
