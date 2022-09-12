package com.babas;

import com.babas.controllers.Categorys;
import com.babas.models.Style;
import com.babas.utilities.Babas;
import com.babas.utilities.Propiedades;
import com.babas.utilities.Utilities;
import com.babas.views.frames.FLogin;

import java.util.Date;

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
//        Style style=new Style();
//        style.setCategory(Categorys.get(1));
//        style.setCode("acs");
//        style.setName("PANTALON DE VESTIR MARCA PALOMITA");
//        style.setUpdated(new Date());
//        style.save();
        FLogin fLogin=new FLogin();
        fLogin.setVisible(true);
    }
}
