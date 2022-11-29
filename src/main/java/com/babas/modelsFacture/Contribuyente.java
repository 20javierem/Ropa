package com.babas.modelsFacture;

import java.util.ArrayList;
import java.util.List;

public class Contribuyente {
    private String token_contribuyente;
    private int id_usuario_vendedor;
    private String tipo_proceso;
    private String tipo_envio;

    public String getToken_contribuyente() {
        return token_contribuyente;
    }

    public void setToken_contribuyente(String token_contribuyente) {
        this.token_contribuyente = token_contribuyente;
    }

    public int getId_usuario_vendedor() {
        return id_usuario_vendedor;
    }

    public void setId_usuario_vendedor(int id_usuario_vendedor) {
        this.id_usuario_vendedor = id_usuario_vendedor;
    }

    public String getTipo_proceso() {
        return tipo_proceso;
    }

    public void setTipo_proceso(String tipo_proceso) {
        this.tipo_proceso = tipo_proceso;
    }

    public String getTipo_envio() {
        return tipo_envio;
    }

    public void setTipo_envio(String tipo_envio) {
        this.tipo_envio = tipo_envio;
    }
}
