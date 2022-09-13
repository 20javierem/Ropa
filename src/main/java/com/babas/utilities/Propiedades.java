package com.babas.utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.AttributedString;
import java.util.Properties;

public class Propiedades {
    private Properties properties;
    private final File carpeta = new File(System.getProperty("user.home") + "/.Tienda-Ropa");
    private File archivo;

    public Propiedades(){
        try {
            inicializar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void inicializar() throws IOException {
        archivo= new File(carpeta.getAbsolutePath()+"/config.properties");
        FileInputStream inputStream;
        if (!carpeta.exists()) {
            System.out.println("Primera ves");
            carpeta.mkdir();
        }
        if(!archivo.exists()){
            archivo.createNewFile();
            inputStream = new FileInputStream(archivo.getAbsolutePath());
            properties= new Properties();
            properties.load(inputStream);
            inputStream.close();
            setKey("2QXDJJUCSSUW2GC");
            guardar();
            setTema("Claro");
            setFont(String.valueOf(new JTextField().getFont().getSize()));
            guardar();
        }else{
            inputStream = new FileInputStream(archivo.getAbsolutePath());
            properties= new Properties();
            properties.load(inputStream);
            inputStream.close();
        }
    }

    public void guardar() {
        try {
            FileOutputStream outputStream = new FileOutputStream(archivo);
            properties.store(outputStream, "[Config file]");
            outputStream.flush();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getUserPassword(){
        String pasword=properties.getProperty("userPassword");
        return pasword==null?"":Utilities.desencriptar(pasword);
    }
    public void setUserPassword(String password){
        properties.put("userPassword",Utilities.encriptar(password));
    }
    public String getUserName(){
        return properties.getProperty("userName");
    }
    public void setUserName(String nameUser){
        properties.put("userName",nameUser);
    }

    public String getKey() {
        return properties.getProperty("key");
    }
    public void setKey(String key){
        properties.put("key",key);
    }
    public void setTema(String tema){
        properties.put("tema", tema);
    }
    public String getTema() {
        return properties.getProperty("tema");
    }
    public String getServerName(){
        return properties.getProperty("userNameServer");
    }
    public void setServerName(String userServer){
        properties.put("userNameServer",userServer);
    }
    public String getServerPassword(){
        String pasword=properties.getProperty("passwordServer");
        return pasword==null?"":Utilities.desencriptar(pasword);
    }
    public void setServerPassword(String password){
        properties.put("passwordServer",Utilities.encriptar(password));
    }
    public void setFont(String fontSize){
        properties.put("fontSize", fontSize);
    }

    public Font getFont() {
        return new JTextField().getFont().deriveFont(Float.parseFloat(properties.getProperty("fontSize")));
    }

}
