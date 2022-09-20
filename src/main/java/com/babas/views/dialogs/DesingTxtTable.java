package com.babas.views.dialogs;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

import javax.swing.*;
import java.awt.*;

public class DesingTxtTable {
    private JPanel contentPane;
    private JLabel string0;
    private JLabel string1;
    private JLabel string2;

    public DesingTxtTable(Component component) {
        contentPane.setBorder(null);
        contentPane.setBackground(component.getBackground());
        this.string0.setForeground(component.getForeground());
        this.string0.setBackground(component.getBackground());
        this.string1.setBackground(new JTextField().getSelectionColor());
        this.string1.setForeground(new JTextField().getSelectedTextColor());
        this.string2.setForeground(component.getForeground());
        this.string2.setBackground(component.getBackground());
    }
    public void setString0(String string0){
        this.string0.setText(string0);
    }
    public void setString1(String string1){
        this.string1.setText(string1);
    }
    public void setString2(String string2){
        this.string2.setText(string2);
    }
    public JPanel getContentPane() {
        return contentPane;
    }
}
