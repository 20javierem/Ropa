package com.babas.modelsFacture;

import java.util.ArrayList;
import java.util.List;

public class NotaCreditoComprobante {
    private NotaCreditoContribuyente contribuyente;
    private NotaCreditoCabecera_comprobante cabecera_comprobante;
    private List<Detalle> detalle = new ArrayList<>();

    public NotaCreditoContribuyente getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(NotaCreditoContribuyente contribuyente) {
        this.contribuyente = contribuyente;
    }

    public NotaCreditoCabecera_comprobante getCabecera_comprobante() {
        return cabecera_comprobante;
    }

    public void setCabecera_comprobante(NotaCreditoCabecera_comprobante cabecera_comprobante) {
        this.cabecera_comprobante = cabecera_comprobante;
    }

    public List<Detalle> getDetalle() {
        return detalle;
    }
}
