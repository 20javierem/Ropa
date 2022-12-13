package com.babas.modelsFacture;

public class Cabecera_comprobante {
    private String observacion="";
    private String tipo_documento;
    private Integer idsucursal;
    private String fecha_comprobante;
    private Double descuento_monto;


    public Cabecera_comprobante(){
    }

    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public String getTipo_documento() {
        return tipo_documento;
    }

    public String getMoneda() {
        return "PEN";
    }

    public Integer getIdsucursal() {
        return idsucursal;
    }

    public void setIdsucursal(Integer idsucursal) {
        this.idsucursal = idsucursal;
    }

    public String getId_condicionpago() {
        return "";
    }

    public String getFecha_comprobante() {
        return fecha_comprobante;
    }

    public void setFecha_comprobante(String fecha_comprobante) {
        this.fecha_comprobante = fecha_comprobante;
    }

    public String getNro_placa() {
        return "";
    }

    public String getNro_orden() {
        return "";
    }

    public String getGuia_remision() {
        return "";
    }

    public Double getDescuento_monto() {
        return descuento_monto;
    }

    public void setDescuento_monto(Double descuento_monto) {
        this.descuento_monto = descuento_monto;
    }

    public Double getDescuento_porcentaje() {
        return 0.0;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
