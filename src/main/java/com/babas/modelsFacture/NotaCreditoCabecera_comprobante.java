package com.babas.modelsFacture;

public class NotaCreditoCabecera_comprobante {
    private String tipo_documento="07";
    private String moneda="PEN";
    private Integer idsucursal;
    private String id_condicionpago="";
    private String fecha_comprobante;
    private String nro_placa="";
    private String nro_orden="";
    private String guia_remision="";
    private Double descuento_monto;
    private String descuento_porcentaje="";
    private String observacion;
    private String doc_modifica_id_tipodoc_electronico;
    private String doc_modifica_serie_comprobante;
    private Long doc_modifica_numero_comprobante;
    private String id_motivo_nota_credito="01";

    public void setIdsucursal(Integer idsucursal) {
        this.idsucursal = idsucursal;
    }

    public void setFecha_comprobante(String fecha_comprobante) {
        this.fecha_comprobante = fecha_comprobante;
    }

    public void setDescuento_monto(Double descuento_monto) {
        this.descuento_monto = descuento_monto;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public void setDoc_modifica_id_tipodoc_electronico(String doc_modifica_id_tipodoc_electronico) {
        this.doc_modifica_id_tipodoc_electronico = doc_modifica_id_tipodoc_electronico;
    }

    public void setDoc_modifica_serie_comprobante(String doc_modifica_serie_comprobante) {
        this.doc_modifica_serie_comprobante = doc_modifica_serie_comprobante;
    }

    public void setDoc_modifica_numero_comprobante(Long doc_modifica_numero_comprobante) {
        this.doc_modifica_numero_comprobante = doc_modifica_numero_comprobante;
    }

    public String getTipo_documento() {
        return tipo_documento;
    }

    public String getMoneda() {
        return moneda;
    }

    public Integer getIdsucursal() {
        return idsucursal;
    }

    public String getId_condicionpago() {
        return id_condicionpago;
    }

    public String getFecha_comprobante() {
        return fecha_comprobante;
    }

    public String getNro_placa() {
        return nro_placa;
    }

    public String getNro_orden() {
        return nro_orden;
    }

    public String getGuia_remision() {
        return guia_remision;
    }

    public Double getDescuento_monto() {
        return descuento_monto;
    }

    public String getDescuento_porcentaje() {
        return descuento_porcentaje;
    }

    public String getObservacion() {
        return observacion;
    }

    public String getDoc_modifica_id_tipodoc_electronico() {
        return doc_modifica_id_tipodoc_electronico;
    }

    public String getDoc_modifica_serie_comprobante() {
        return doc_modifica_serie_comprobante;
    }

    public Long getDoc_modifica_numero_comprobante() {
        return doc_modifica_numero_comprobante;
    }

    public String getId_motivo_nota_credito() {
        return id_motivo_nota_credito;
    }



}
