package com.babas.modelsFacture;


public class Contribuyente {
    private String token_contribuyente;
    private Integer id_usuario_vendedor;
    private String tipo_proceso="produccion";
    private String tipo_envio="inmediato";

    public String getToken_contribuyente() {
        return token_contribuyente;
    }

    public void setToken_contribuyente(String token_contribuyente) {
        this.token_contribuyente = token_contribuyente;
    }

    public int getId_usuario_vendedor() {
        return id_usuario_vendedor;
    }

    public void setId_usuario_vendedor(Integer id_usuario_vendedor) {
        this.id_usuario_vendedor = id_usuario_vendedor;
    }

    public String getTipo_proceso() {
        return tipo_proceso;
    }

    public String getTipo_envio() {
        return tipo_envio;
    }

}
