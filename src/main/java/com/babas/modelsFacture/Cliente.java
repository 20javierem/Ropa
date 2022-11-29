package com.babas.modelsFacture;

public class Cliente {
    private int tipo_docidentidad;
    private String numerodocumento="";
    private String nombre="";
    private String email="";
    private String direccion="";
    private String ubigeo="";
    private String sexo="";
    private String fecha_nac="";
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

    public String getEmail() {
        return email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public String getSexo() {
        return sexo;
    }

    public String getFecha_nac() {
        return fecha_nac;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
}
