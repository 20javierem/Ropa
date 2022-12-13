package com.babas.modelsFacture;

public class CancelCabecera_comprobante {
    private String tipo_documento;
    private Long numero_comprobante;
    private String serie_comprobante;


    public String getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public String getMotivo_anulacion() {
        return "Error en documento";
    }

    public Long getNumero_comprobante() {
        return numero_comprobante;
    }

    public void setNumero_comprobante(Long numero_comprobante) {
        this.numero_comprobante = numero_comprobante;
    }

    public String getSerie_comprobante() {
        return serie_comprobante;
    }

    public void setSerie_comprobante(String serie_comprobante) {
        this.serie_comprobante = serie_comprobante;
    }
}