package com.babas.views.frames;

import com.babas.custom.CustomPasswordField;
import com.babas.custom.JPanelGradiente;
import com.babas.utilities.Propiedades;
import com.babas.utilities.Utilities;

import javax.swing.*;
import java.awt.*;

public class FLogin extends JFrame {
    private JPanel contentPane;
    private JPanel panelDatos;
    private JButton btnInitSession;
    private JCheckBox recordarUsuarioCheckBox;
    private JTextField fieldUser;
    private JLabel logoLogin;
    private CustomPasswordField fieldPasword;
    private JLabel lblError;
    private JButton btnShowPasword;

    public FLogin() {
        initComponents();
    }

    private void initComponents(){
        setContentPane(contentPane);
        setTitle("Login");
        Utilities.setJFrame(this);
        getRootPane().setDefaultButton(btnInitSession);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        btnInitSession.addActionListener(e -> start());

    }

    private void start(){
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if (true){
            FPrincipal fPrincipal =new FPrincipal();
            dispose();
            fPrincipal.setVisible(true);
        }else{
            lblError.setVisible(true);
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        contentPane=new JPanelGradiente(new Color(0xF3E749),new Color(0xDA3FBB));
    }
}
