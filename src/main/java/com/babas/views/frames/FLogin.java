package com.babas.views.frames;

import com.babas.controllers.Users;
import com.babas.custom.CustomPasswordField;
import com.babas.custom.JPanelGradiente;
import com.babas.models.User;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;

import javax.swing.*;
import java.awt.*;

public class FLogin extends JFrame {
    private JPanel contentPane;
    private JPanel panelDatos;
    private JButton btnInitSession;
    private JCheckBox ckRememberUser;
    private JTextField fieldUser;
    private JLabel logoLogin;
    private CustomPasswordField fieldPasword;
    private JLabel lblError;
    private JButton btnShowPasword;

    public FLogin() {
        initComponents();
        btnInitSession.addActionListener(e -> start());
    }

    private void initComponents(){
        setContentPane(contentPane);
        setTitle("Login");
        Utilities.setJFrame(this);
        getRootPane().setDefaultButton(btnInitSession);
        loadUserSaved();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }
    private void loadUserSaved(){
        String userName=Utilities.propiedades.getUserName();
        String userPassword=Utilities.propiedades.getUserPassword();;
        if(userName!=null){
            fieldUser.setText(userName);
            fieldPasword.setText(userPassword);
            ckRememberUser.setSelected(true);
        }
    }

    private void start(){
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        String userName=fieldUser.getText().trim();
        String userPassword=new String(fieldPasword.getPassword());
        if(!userName.isBlank()&&!userPassword.isBlank()){
            User user=Users.getByUserName(userName);
            if(user!=null){
                if(user.getUserPassword().equals(userPassword)){
                    Babas.user=user;
                    saveUser();
                    FPrincipal fPrincipal=new FPrincipal();
                    fPrincipal.setVisible(true);
                    dispose();
                }else{
                    lblError.setText("Credenciales incorrectas");
                    lblError.setVisible(true);
                }
            }else{
                lblError.setText("Credenciales incorrectas");
                lblError.setVisible(true);
            }
        }else{
            lblError.setText("Ingrese Usuario y Contraseña");
            lblError.setVisible(true);
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private void saveUser(){
        if(ckRememberUser.isSelected()){
            Utilities.propiedades.setUserName(fieldUser.getText().trim());
            Utilities.propiedades.setUserPassword(new String(fieldPasword.getPassword()));
        }else{
            Utilities.propiedades.setUserName(null);
            Utilities.propiedades.setUserPassword(null);
        }
        Utilities.propiedades.guardar();
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        contentPane=new JPanelGradiente(new Color(0xF3E749),new Color(0xDA3FBB));
    }
}
