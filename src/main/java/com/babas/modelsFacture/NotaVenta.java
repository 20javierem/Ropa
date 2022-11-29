package com.babas.modelsFacture;

import java.util.ArrayList;
import java.util.List;

public class NotaVenta {
    private Contribuyente contribuyente;
    private Cliente cliente;
    private Cabecera_comprobante cabecera_comprobante;
    private List<Detalle> detalle = new ArrayList<>();

    public Contribuyente getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(Contribuyente contribuyente) {
        this.contribuyente = contribuyente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cabecera_comprobante getCabecera_comprobante() {
        return cabecera_comprobante;
    }

    public void setCabecera_comprobante(Cabecera_comprobante cabecera_comprobante) {
        this.cabecera_comprobante = cabecera_comprobante;
    }

    public List<Detalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<Detalle> detalle) {
        this.detalle = detalle;
    }
}
