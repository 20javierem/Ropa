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
import com.moreno.Notify;

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
        Utilities.propiedades.save();
        Utilities.loadTheme();

        JFrame jFrame=new JFrame();
        jFrame.setLocationRelativeTo(null);
        Utilities.setJFrame(jFrame);

        Babas.initialize();
        if(Babas.state){
            Babas.company=Companys.get(1);
            if(Babas.company==null){
                DCompany dCompany=new DCompany();
                dCompany.setVisible(true);
            }else{
                if(Babas.company.getLogo()!=null){
                    if(Utilities.openConection()){
                        Utilities.downloadLogo(Babas.company.getLogo());
                    }
                }
            }

            if(Babas.company!=null){
                if(Branchs.getTodos().isEmpty()){
                    DBranch dBranch=new DBranch(new Branch());
                    dBranch.setVisible(true);
                }
            }

            if(Babas.company!=null&&!Branchs.getTodos().isEmpty()){
                if(Users.getTodos().isEmpty()){
                    User user=new User();
                    user.getBranchs().add(Branchs.get(1));
                    user.setPermitions(new Permission(true));
                    Branchs.get(1).getUsers().add(user);
                    DUser dUser=new DUser(true,user);
                    dUser.setVisible(true);
                }
            }

            if(!Users.getTodos().isEmpty()){
                FLogin fLogin=new FLogin();
                fLogin.setVisible(true);
            }
        }else{
            Notify.sendNotify(Utilities.getJFrame(), Notify.Type.WARNING, Notify.Location.TOP_CENTER,"ERROR","No hay conexión con la base de datos");
            try {
                Thread.sleep(5000);
                System.exit(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
