package com.babas.utilitiesTables.buttonEditors;

import com.babas.App;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.icons.FlatClearIcon;
import com.formdev.flatlaf.icons.FlatTabbedPaneCloseIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JButtonAction extends JButton {
    private static Icon iconError=new FlatSVGIcon(App.class.getResource("icons/svg/error.svg"));
    private static Icon iconCheck=new FlatSVGIcon(App.class.getResource("icons/svg/check.svg"));
    private static Icon iconEdit=new FlatSVGIcon(App.class.getResource("icons/svg/edit.svg"));
    private static Icon iconChange=new FlatSVGIcon(App.class.getResource("icons/svg/changeVoucher.svg"));
    private static Icon iconShow=new FlatSVGIcon(App.class.getResource("icons/svg/show.svg"));

//    private static Icon iconError=new ImageIcon(App.class.getResource("icons/x16/eliminar.png"));
//    private static Icon iconCheck=new ImageIcon(App.class.getResource("icons/x16/checkbox.png"));
//    private static Icon iconEdit=new ImageIcon(App.class.getResource("icons/x16/editar.png"));
//    private static Icon iconChange=new ImageIcon(App.class.getResource("icons/x16/change.png"));
//    private static Icon iconShow=new ImageIcon(App.class.getResource("icons/x16/mostrarContraseña.png"));

    public JButtonAction(String icon, String text) {
        setIcon(new ImageIcon(App.class.getResource("icons/"+icon)));
        setText(text);
        initialize();
    }

    public JButtonAction(String icon) {
        switch (icon){
            case "error":
                setIcon(iconError);
                break;
            case "edit":
                setIcon(iconEdit);
                break;
            case "change":
                setIcon(iconChange);
                break;
            case "show":
                setIcon(iconShow);
                break;
            case "check":
                setIcon(iconCheck);
                break;
        }
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