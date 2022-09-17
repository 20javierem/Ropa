package com.babas;

import com.babas.controllers.Brands;
import com.babas.controllers.Categorys;
import com.babas.controllers.Sexs;
import com.babas.controllers.Styles;
import com.babas.models.Brand;
import com.babas.models.Sex;
import com.babas.models.Style;
import com.babas.utilities.Babas;
import com.babas.utilities.Propiedades;
import com.babas.utilities.Utilities;
import com.babas.views.frames.FLogin;
import com.babas.views.frames.PruebaList;

import java.util.Date;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {

        Utilities.propiedades=new Propiedades();
        Babas.initialize();
        Utilities.loadTheme();
//        PruebaList pruebaList=new PruebaList();
//        pruebaList.setVisible(true);
        FLogin fLogin=new FLogin();
        fLogin.setVisible(true);
    }
}
