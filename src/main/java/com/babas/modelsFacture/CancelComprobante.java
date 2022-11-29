package com.babas.modelsFacture;

public class CancelComprobante {
    private CancelContribuyente contribuyente;
    private CancelCabecera_comprobante cabecera_comprobante;

    public CancelContribuyente getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(CancelContribuyente contribuyente) {
        this.contribuyente = contribuyente;
    }

    public CancelCabecera_comprobante getCabecera_comprobante() {
        return cabecera_comprobante;
    }

    public void setCabecera_comprobante(CancelCabecera_comprobante cabecera_comprobante) {
        this.cabecera_comprobante = cabecera_comprobante;
    }
}
