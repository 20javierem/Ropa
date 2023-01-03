package com.babas.modelsFacture;

public class Cabecera_comprobante {
    private String observacion="";
    private String tipo_documento;
    private Integer idsucursal;
    private String fecha_comprobante;
    private Double descuento_monto;
    private final String moneda="PEN";
    private final String id_condicionpago="";
    private final String nro_placa="";
    private final String nro_orden="";
    private final String guia_remision="";
    private final Double descuento_porcentaje=0.0;

    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public String getTipo_documento() {
        return tipo_documento;
    }

    public Integer getIdsucursal() {
        return idsucursal;
    }

    public void setIdsucursal(Integer idsucursal) {
        this.idsucursal = idsucursal;
    }

    public String getFecha_comprobante() {
        return fecha_comprobante;
    }

    public void setFecha_comprobante(String fecha_comprobante) {
        this.fecha_comprobante = fecha_comprobante;
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
