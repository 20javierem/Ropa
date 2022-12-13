package com.babas.modelsFacture;

import com.babas.models.Client;
import com.babas.models.Rental;
import com.babas.models.Sale;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.views.frames.FPrincipal;
import com.google.gson.Gson;
import com.moreno.Notify;
import com.squareup.okhttp.*;
import org.objectweb.asm.commons.Method;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ApiClient {
    private static final OkHttpClient client=new OkHttpClient();
    private static final MediaType mediaType = MediaType.parse("application/json");
    private static RequestBody body;
    private static Request request;
    private static Response response;

    public static boolean sendComprobante(Comprobante comprobante,boolean showMessage) {
        String url;
        if ("77".equals(comprobante.getCabecera_comprobante().getTipo_documento())) {
            url = "https://facturadorbabas.com/facturacion/api/procesar_nota_venta";
        } else {
            url = "https://facturadorbabas.com/facturacion/api/procesar_venta";
        }
        try {
            body = RequestBody.create(mediaType,new Gson().toJson(comprobante));
            request = new Request.Builder().
                    url(url).
                    method("POST", body).
                    addHeader("Authorization", "Bearer " + Babas.company.getToken()).
                    addHeader("Content-Type", "application/json").
                    build();
            client.setConnectTimeout(15, TimeUnit.SECONDS);
            response = client.newCall(request).execute();
            if(response.code()==200){
                ResponseJson responseJson=new Gson().fromJson(response.body().string(), ResponseJson.class);
                if(responseJson.getRespuesta().equals("ok")){
                    if(showMessage){
                        Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Comprobante enviado a sunat");
                    }
                    return true;
                }
                Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR",responseJson.getMensaje());
                return false;
            }else{
                return false;
            }
        }catch (UnknownHostException e){
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No hay conexión a internet");
        } catch (IOException e) {
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Sucedió un error inesperado");
        }
        return false;
    }

    public static boolean cancelComprobante(CancelComprobante cancelComprobante) {
        String url="https://facturadorbabas.com/facturacion/api/comunicacion_baja";
        try {
            body = RequestBody.create(mediaType,new Gson().toJson(cancelComprobante));
            request = new Request.Builder().
                    url(url).
                    method("POST", body).
                    addHeader("Authorization", "Bearer " + Babas.company.getToken()).
                    addHeader("Content-Type", "application/json").
                    build();
            client.setConnectTimeout(15, TimeUnit.SECONDS);
            response = client.newCall(request).execute();
            if(response.code()==200){
                ResponseJson responseJson=new Gson().fromJson(response.body().string(), ResponseJson.class);
                if(responseJson.getRespuesta().equals("ok")){
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.SUCCESS, Notify.Location.TOP_CENTER,"ÉXITO","Comprobante cancelado ante sunat");
                    return true;
                }else{
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR",responseJson.getMensaje());
                    return false;
                }
            }else{
                return false;
            }
        } catch (UnknownHostException e){
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No hay conexión a internet");
        } catch (IOException e) {
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Sucedió un error inesperado");
        }
        return false;
    }

    public static Client getClient(String document){
        String url;
        int typeDocument=document.length()==8?1:document.length()==11?6:0;
        if(typeDocument==1){
            url="https://api.perudevs.com/api/v1/dni/simple?document="+document+"&key="+Babas.company.getTokenConsults();
        }else if(document.length()==11){
            url="https://api.perudevs.com/api/v1/ruc?document="+document+"&key="+Babas.company.getTokenConsults();
        }else{
            return null;
        }
        try {
            request = new Request.Builder().
                    url(url).
                    method("GET", null).
                    build();
            client.setConnectTimeout(10, TimeUnit.SECONDS);
            response = client.newCall(request).execute();
            if(response.code()==200){
                ClientResponse clientResponse=new Gson().fromJson(response.body().string(), ClientResponse.class);
                if(clientResponse.isEstado()){
                    Client client=new Client();
                    client.setDni(document);
                    client.setNames(clientResponse.getResultado().getNames());
                    client.setPhone("");
                    client.setAddres("");
                    client.setTypeDocument(typeDocument);
                    client.save();
                    FPrincipal.clients.add(client);
                    return client;
                } else {
                    Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR",clientResponse.getMensaje());
                }
            }else{
                System.out.println(response.code());
                System.out.println(response.body());
                System.out.println(response.message());
            }
        } catch (UnknownHostException e){
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No hay conexión a internet");
        } catch (IOException e) {
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","Sucedió un error inesperado");
        }
        return null;
    }

    public static Comprobante getComprobanteOfSale(Sale sale){
        Comprobante comprobante=new Comprobante();
        Contribuyente contribuyente=new Contribuyente();
        contribuyente.setToken_contribuyente(Babas.company.getToken());
        contribuyente.setId_usuario_vendedor(sale.getUser().getIdFact());
        comprobante.setContribuyente(contribuyente);
        Cabecera_comprobante cabecera_comprobante=new Cabecera_comprobante();
        cabecera_comprobante.setIdsucursal(sale.getBranch().getIdFact());
        cabecera_comprobante.setTipo_documento(sale.getTypeVoucher());
        cabecera_comprobante.setFecha_comprobante(Utilities.formatoFecha.format(sale.getCreated()));
        cabecera_comprobante.setDescuento_monto(sale.getDiscount());
        cabecera_comprobante.setObservacion(sale.getObservation());
        comprobante.setCabecera_comprobante(cabecera_comprobante);
        Cliente cliente=new Cliente();
        if(sale.getClient()!=null){
            cliente.setCelular(sale.getClient().getPhone());
            cliente.setDireccion(sale.getClient().getAddres());
            cliente.setNombre(sale.getClient().getNames());
            cliente.setNumerodocumento(sale.getClient().getDni());
            cliente.setTipo_docidentidad(sale.getClient().getTypeDocument());
        }
        comprobante.setCliente(cliente);
        sale.getDetailSales().forEach(detailSale -> {
            Detalle detalle=new Detalle();
            detalle.setCantidad(detailSale.getQuantity());
            detalle.setCodigo(detailSale.getCodeProduct());
            detalle.setDescripcion(detailSale.getProductString()+" "+detailSale.getNamePresentation());
            detalle.setPrecio_venta(detailSale.getPrice());
            detalle.setIdproducto(Babas.company.getIdProduct());
            comprobante.getDetalle().add(detalle);
        });
        return comprobante;
    }

    public static Comprobante getComprobanteOfRental(Rental rental){
        Comprobante comprobante=new Comprobante();
        Contribuyente contribuyente=new Contribuyente();
        contribuyente.setToken_contribuyente(Babas.company.getToken());
        contribuyente.setId_usuario_vendedor(rental.getUser().getIdFact());
        comprobante.setContribuyente(contribuyente);
        Cabecera_comprobante cabecera_comprobante=new Cabecera_comprobante();
        cabecera_comprobante.setIdsucursal(rental.getBranch().getIdFact());
        cabecera_comprobante.setTipo_documento(rental.getTypeVoucher());
        cabecera_comprobante.setFecha_comprobante(Utilities.formatoFecha.format(rental.getCreated()));
        cabecera_comprobante.setDescuento_monto(rental.getDiscount());
        cabecera_comprobante.setObservacion(rental.getObservation());
        comprobante.setCabecera_comprobante(cabecera_comprobante);
        Cliente cliente=new Cliente();
        if(rental.getClient()!=null){
            cliente.setCelular(rental.getClient().getPhone());
            cliente.setDireccion(rental.getClient().getAddres());
            cliente.setNombre(rental.getClient().getNames());
            cliente.setNumerodocumento(rental.getClient().getDni());
            cliente.setTipo_docidentidad(rental.getClient().getTypeDocument());
        }
        comprobante.setCliente(cliente);
        rental.getDetailRentals().forEach(detailSale -> {
            Detalle detalle=new Detalle();
            detalle.setCantidad(detailSale.getQuantity());
            detalle.setCodigo(detailSale.getCodeProduct());
            detalle.setDescripcion(detailSale.getProductString()+" "+detailSale.getNamePresentation());
            detalle.setPrecio_venta(detailSale.getPrice());
            detalle.setIdproducto(Babas.company.getIdProduct());
            comprobante.getDetalle().add(detalle);
        });
        return comprobante;
    }

    public static CancelComprobante getCancelComprobanteOfSale(Sale sale){
        CancelComprobante cancelComprobante=new CancelComprobante();
        CancelContribuyente contribuyente=new CancelContribuyente();
        contribuyente.setToken_contribuyente(Babas.company.getToken());
        contribuyente.setId_usuario_vendedor(sale.getUser().getIdFact());
        cancelComprobante.setContribuyente(contribuyente);
        CancelCabecera_comprobante cabecera_comprobante=new CancelCabecera_comprobante();
        cabecera_comprobante.setNumero_comprobante(sale.getCorrelativo());
        if(sale.getTypeVoucher().equals("77")){
            cabecera_comprobante.setSerie_comprobante("");
        }else{
            cabecera_comprobante.setSerie_comprobante(sale.getSerie());
        }
        cabecera_comprobante.setTipo_documento(sale.getTypeVoucher());
        cancelComprobante.setCabecera_comprobante(cabecera_comprobante);
        return cancelComprobante;
    }

    public static CancelComprobante getCancelComprobanteOfRental(Rental rental){
        CancelComprobante cancelComprobante=new CancelComprobante();
        CancelContribuyente contribuyente=new CancelContribuyente();
        contribuyente.setToken_contribuyente(Babas.company.getToken());
        contribuyente.setId_usuario_vendedor(rental.getUser().getIdFact());
        cancelComprobante.setContribuyente(contribuyente);
        CancelCabecera_comprobante cabecera_comprobante=new CancelCabecera_comprobante();
        cabecera_comprobante.setNumero_comprobante(rental.getCorrelativo());
        if(rental.getTypeVoucher().equals("77")){
            cabecera_comprobante.setSerie_comprobante("");
        }else{
            cabecera_comprobante.setSerie_comprobante(rental.getSerie());
        }
        cabecera_comprobante.setTipo_documento(rental.getTypeVoucher());
        cancelComprobante.setCabecera_comprobante(cabecera_comprobante);
        return cancelComprobante;
    }

}
