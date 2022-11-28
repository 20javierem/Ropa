package com.babas;

import com.babas.controllers.*;
import com.babas.models.*;
import com.babas.utilities.Babas;
import com.babas.utilities.Propiedades;
import com.babas.utilities.Utilities;
import com.babas.views.dialogs.DBranch;
import com.babas.views.dialogs.DCompany;
import com.babas.views.dialogs.DCrop;
import com.babas.views.dialogs.DUser;
import com.babas.views.frames.FLogin;

import javax.swing.*;

public class App
{
    public static void main( String[] args ) {
        Utilities.propiedades=new Propiedades();
        Utilities.propiedades.save();
        Utilities.loadTheme();
        FLogin fLogin=new FLogin();
    }

}
