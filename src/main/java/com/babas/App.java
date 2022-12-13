package com.babas;

import com.babas.controllers.*;
import com.babas.models.*;
import com.babas.modelsFacture.*;
import com.babas.utilities.Babas;
import com.babas.utilities.Propiedades;
import com.babas.utilities.Utilities;
import com.babas.views.dialogs.*;
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
        fLogin.tryConnection();
        fLogin.setVisible(true);
    }

}
