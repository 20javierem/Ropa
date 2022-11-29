package com.babas.modelsFacture;

public class Cabecera_comprobante {
    private String moneda="PEN";
    private String nro_placa="";
    private String nro_orden="";
    private String guia_remision="";
    private String id_condicionpago="";
    private String observacion="";
    private String tipo_documento;
    private Integer idsucursal;
    private String fecha_comprobante;
    private Double descuento_monto;
    private Double descuento_porcentaje;


    public Cabecera_comprobante(){
    }

    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
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

    public void setIdsucursal(Integer idsucursal) {
        this.idsucursal = idsucursal;
    }

    public String getId_condicionpago() {
        return id_condicionpago;
    }

    public String getFecha_comprobante() {
        return fecha_comprobante;
    }

    public void setFecha_comprobante(String fecha_comprobante) {
        this.fecha_comprobante = fecha_comprobante;
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

    public void setDescuento_monto(Double descuento_monto) {
        this.descuento_monto = descuento_monto;
    }

    public Double getDescuento_porcentaje() {
        return descuento_porcentaje;
    }

    public void setDescuento_porcentaje(Double descuento_porcentaje) {
        this.descuento_porcentaje = descuento_porcentaje;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
