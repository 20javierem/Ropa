package com.babas.utilitiesTables.buttonEditors;

import com.babas.App;
import com.formdev.flatlaf.icons.FlatClearIcon;
import com.formdev.flatlaf.icons.FlatTabbedPaneCloseIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JButtonAction extends JButton {

    public JButtonAction(String icon, String texto) {
        setIcon(new ImageIcon(App.class.getResource("icons/"+icon)));
        setText(texto);
        setHorizontalTextPosition(2);
        initialize();
    }

    public JButtonAction(String icon) {
        setIcon(new ImageIcon(App.class.getResource("icons/"+icon)));
        initialize();
    }

    private void initialize() {
        setBorder(null);
        setOpaque(false);
        setBorderPainted(false);
        setFocusable(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(new JTable().getSelectionBackground());
            }
        });
    }
    public void changeIcon(String icon){
        setIcon(new ImageIcon(App.class.getResource("icons/"+icon)));
    }
}