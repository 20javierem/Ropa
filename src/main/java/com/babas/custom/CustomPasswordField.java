package com.babas.custom;

import com.babas.App;
import com.formdev.flatlaf.extras.components.FlatPasswordField;
import com.formdev.flatlaf.icons.FlatRevealIcon;

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
                    btnShowPasword.setIcon(new FlatRevealIcon());
                } else {
                    setEchoChar('•');
                    btnShowPasword.setIcon(new FlatRevealIcon());
                }
            }
        });
        btnShowPasword.setIcon(new FlatRevealIcon());
        btnShowPasword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        setTrailingComponent(btnShowPasword);
    }

    @Override
    public void setText(String t) {
        super.setText(t);
        if(getPassword().length>0){
            btnShowPasword.setVisible(true);
        }
    }
}
