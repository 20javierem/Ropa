package com.babas.utilities;

import com.babas.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class PropiedadesFlatlLaf {
    private Properties properties;
    private File file;

    public PropiedadesFlatlLaf(){
        try {
            inicializar();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void inicializar() throws IOException, URISyntaxException {
        file= new File(App.class.getResource("themes/FlatLaf.properties").toURI());
        properties= new Properties();
        properties.load(App.class.getResourceAsStream("themes/FlatLaf.properties"));
    }

    public void guardar() {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            properties.store(outputStream,"coment");
            outputStream.flush();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setFontSize(String font){
        properties.put("defaultFont", font);
    }

    public String getFontSize() {
        return properties.getProperty("defaultFont");
    }
}
