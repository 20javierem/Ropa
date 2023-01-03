package com.babas.modelsFacture;

public class Cliente {
    private int tipo_docidentidad=1;
    private String numerodocumento="00000000";
    private String nombre="";
    private final String email="";
    private String direccion="";
    private final String ubigeo="";
    private final String sexo="";
    private final String fecha_nac="";
    private String celular="";


    public int getTipo_docidentidad() {
        return tipo_docidentidad;
    }

    public void setTipo_docidentidad(int tipo_docidentidad) {
        this.tipo_docidentidad = tipo_docidentidad;
    }

    public String getNumerodocumento() {
        return numerodocumento;
    }

    public void setNumerodocumento(String numerodocumento) {
        this.numerodocumento = numerodocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
}
