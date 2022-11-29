package com.babas.modelsFacture;

import com.google.gson.Gson;
import com.squareup.okhttp.*;

import java.awt.*;
import java.io.IOException;

public class ApiClient {
    private static OkHttpClient client=new OkHttpClient();
    private static MediaType mediaType = MediaType.parse("application/json");
    private static RequestBody body;
    private static Request request;
    private static Response response;

    public static void sendNotaVenta(NotaVenta notaVenta) {
        try {
            body = RequestBody.create(mediaType,new Gson().toJson(notaVenta));
            request = new Request.Builder().
                    url("https://facturadorbabas.com/facturacion/api/procesar_nota_venta").
                    method("POST", body).
                    addHeader("Authorization", "Bearer " + "RIB7KXH15AUW5BG8A57IJB9PHXIF1XT8LWWOP").
                    addHeader("Content-Type", "application/json").
                    build();
            response = client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
