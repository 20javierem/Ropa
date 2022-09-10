package com.babas;

import com.babas.utilities.Babas;
import com.babas.utilities.Propiedades;
import com.babas.utilities.Utilities;
import com.babas.views.frames.FLogin;

import javax.swing.*;
import java.awt.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Utilities.propiedades=new Propiedades();
        Babas.initialize();
        Utilities.loadTheme();
        FLogin fLogin=new FLogin();
        fLogin.setVisible(true);
    }
}
