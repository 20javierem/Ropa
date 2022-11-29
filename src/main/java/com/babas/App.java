package com.babas;

import com.babas.controllers.*;
import com.babas.models.*;
import com.babas.modelsFacture.*;
import com.babas.utilities.Babas;
import com.babas.utilities.Propiedades;
import com.babas.utilities.Utilities;
import com.babas.views.dialogs.DBranch;
import com.babas.views.dialogs.DCompany;
import com.babas.views.dialogs.DCrop;
import com.babas.views.dialogs.DUser;
import com.babas.views.frames.FLogin;
import com.google.gson.Gson;

import javax.swing.*;

public class App
{
    public static void main( String[] args ) {
        Utilities.propiedades=new Propiedades();
        Utilities.propiedades.save();
        Utilities.loadTheme();
        FLogin fLogin=new FLogin();
        fLogin.setVisible(true);
        fLogin.tryConnection();

//        NotaVenta notaVenta=new NotaVenta();
//        Contribuyente contribuyente=new Contribuyente();
//        contribuyente.setToken_contribuyente("RIB7KXH15AUW5BG8A57IJB9PHXIF1XT8LWWOP");
//        contribuyente.setTipo_proceso("prueba");
//        contribuyente.setId_usuario_vendedor(3);
//        contribuyente.setTipo_envio("inmediato");
//        notaVenta.setContribuyente(contribuyente);
//
//        Cliente cliente=new Cliente();
//        cliente.setTipo_docidentidad(6);
//        cliente.setNumerodocumento("10620205546");
//        cliente.setNombre("Javier Ernesto Moreno Lloclle");
//        notaVenta.setCliente(cliente);
//
//        Cabecera_comprobante cabecera_comprobante=new Cabecera_comprobante();
//        cabecera_comprobante.setFecha_comprobante("28/11/2022");
//        cabecera_comprobante.setDescuento_monto(0.0);
//        cabecera_comprobante.setIdsucursal(2);
//        cabecera_comprobante.setDescuento_porcentaje(0.0);
//        notaVenta.setCabecera_comprobante(cabecera_comprobante);
//
//        Detalle detalle=new Detalle();
//        detalle.setCodigo("TV_CODIGOPROD2");
//        detalle.setIdproducto(7);
//        detalle.setDescripcion("Producto de Ejemplo");
//        detalle.setPrecio_venta(2.00);
//        detalle.setCantidad(2);
//        notaVenta.getDetalle().add(detalle);
//
//        detalle=new Detalle();
//        detalle.setCodigo("TV_CODIGOPROD3");
//        detalle.setIdproducto(7);
//        detalle.setDescripcion("Producto de Ejemplo 2");
//        detalle.setPrecio_venta(4.00);
//        detalle.setCantidad(2);
//
//        notaVenta.getDetalle().add(detalle);
//
//        ApiClient.sendNotaVenta(notaVenta);

    }

}
