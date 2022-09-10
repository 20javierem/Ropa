package com.babas.custom;

import com.babas.App;
import com.formdev.flatlaf.extras.components.FlatPasswordField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomPasswordField extends FlatPasswordField {
    private JButton btnShowPasword;

    public CustomPasswordField(){
        loadShowPasword();
    }

    private void loadShowPasword() {
        btnShowPasword=new JButton();
        btnShowPasword.setVisible(false);
        btnShowPasword.setContentAreaFilled(false);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                btnShowPasword.setVisible(getPassword().length > 0);
            }
        });
        btnShowPasword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (getEchoChar() == '•') {
                    setEchoChar((char) 0);
                    btnShowPasword.setIcon(new ImageIcon(App.class.getResource("icons/x16/mostrarContraseña.png")));
                } else {
                    setEchoChar('•');
                    btnShowPasword.setIcon(new ImageIcon(App.class.getResource("icons/x16/ocultarContraseña.png")));
                }
            }
        });
        btnShowPasword.setIcon(new ImageIcon(App.class.getResource("icons/x16/ocultarContraseña.png")));
        btnShowPasword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        putClientProperty("JTextField.trailingComponent",btnShowPasword);
    }
}
