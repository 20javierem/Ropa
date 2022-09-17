package com.babas.views.frames;

import com.babas.models.Product;

import javax.swing.*;

public class Diseño {
    private JPanel contentPane;
    private JLabel lblName;
    private JCheckBox ckActive;

    public Diseño(Product product){
        lblName.setText(product.getStyle().getName());
        ckActive.setSelected(product.isActive());
    }

    public JPanel getContentPane(){
        return contentPane;
    }
}
