package com.babas;

import com.babas.controllers.*;
import com.babas.models.*;
import com.babas.models.Color;
import com.babas.utilities.Babas;
import com.babas.utilities.Propiedades;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.BranchAbstractModel;
import com.babas.views.dialogs.DBranch;
import com.babas.views.dialogs.DCompany;
import com.babas.views.dialogs.DUser;
import com.babas.views.frames.FLogin;
import com.babas.views.frames.FPrincipal;
import com.babas.views.frames.PruebaList;

import javax.swing.*;
import java.awt.*;
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
        Utilities.propiedades.setServerPassword("ernestomoreno");
        Utilities.propiedades.setServerName("javier");
        Utilities.propiedades.setServerUrl("192.168.0.119");
        Utilities.propiedades.save();
        Babas.initialize();
        Utilities.loadTheme();

//        Category category=Categorys.get(1);
//        Brand brand=Brands.get(1);
//        Color color=Colors.get(1);
//        Size size=Sizes.get(1);
//        Sex sex=Sexs.get(1);
//
//        for(int i=0;i<1000;i++){
//            Style style=new Style();
//            style.setName("producto 100"+i);
//            style.setCategory(category);
//            style.save();
//            Product product=new Product();
//            product.setSize(size);
//            product.setColor(color);
//            product.setBrand(brand);
//            product.setStyle(style);
//            product.setSex(sex);
//            Presentation presentation=new Presentation(product);
//            presentation.setQuantity(1);
//            presentation.setName("UNIDAD");
//            Price price=new Price(presentation);
//            price.setPrice(120.00);
//            price.setDefault(true);
//            presentation.getPrices().add(price);
//            presentation.setPriceDefault(price);
//            product.getPresentations().add(presentation);
//            product.save();
//        }

        Company company=Companys.get(1);
        JFrame jFrame=new JFrame();
        jFrame.setLocationRelativeTo(null);
        Utilities.setJFrame(jFrame);

        if(company==null){
            DCompany dCompany=new DCompany();
            dCompany.setVisible(true);
        }else{
            Babas.company=company;
            if(!company.getLogo().isBlank()){
                Utilities.downloadLogo(company.getLogo());
            }
        }

        if(Babas.company!=null){
            if(Users.getTodos().isEmpty()){
                DUser dUser=new DUser(new User());
                dUser.setVisible(true);
            }
        }

        if(!Users.getTodos().isEmpty()){
            if(Branchs.getTodos().isEmpty()){
                DBranch dBranch=new DBranch(new Branch());
                dBranch.setVisible(true);
            }
        }
        if(!Branchs.getTodos().isEmpty()){
            FLogin fLogin=new FLogin();
            fLogin.setVisible(true);
        }
    }
}
