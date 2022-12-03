package com.babas.utilitiesTables.buttonEditors;

import com.babas.App;
import com.formdev.flatlaf.icons.FlatClearIcon;
import com.formdev.flatlaf.icons.FlatTabbedPaneCloseIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JButtonAction extends JButton {

    public JButtonAction(String icon, String text) {
        setIcon(new ImageIcon(App.class.getResource("icons/"+icon)));
        setText(text);
        initialize();
    }

    public JButtonAction(String icon) {
        setIcon(new ImageIcon(App.class.getResource("icons/"+icon)));
        initialize();
    }

    public JButtonAction(Icon icon){
        setIcon(icon);
        initialize();
    }
    public JButtonAction(Icon icon,String text){
        setIcon(icon);
        setText(text);
        initialize();
    }

    private void initialize() {
        setHorizontalTextPosition(2);
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