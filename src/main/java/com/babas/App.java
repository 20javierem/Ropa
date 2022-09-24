package com.babas;

import com.babas.controllers.*;
import com.babas.models.*;
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
        Babas.initialize();
        Utilities.loadTheme();

        Company company=Companys.get(1);
        JFrame jFrame=new JFrame();
        jFrame.setLocationRelativeTo(null);
        Utilities.setJFrame(jFrame);

        if(company==null){
            DCompany dCompany=new DCompany();
            dCompany.setVisible(true);
        }else{
            Babas.company=company;
        }

        if(Babas.company.getId()!=null){
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
