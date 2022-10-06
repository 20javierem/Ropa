package com.babas.views;

import com.babas.models.Product;
import com.babas.utilities.Utilities;

import javax.swing.*;

public class ModelProduct {
    private JPanel contentPane;
    private JLabel lblName;
    private JLabel lblPrice;
    private JLabel lblBrand;
    private JLabel lblSize;
    private JLabel lblIcon;
    private Product product;

    public ModelProduct(Product product) {
        this.product = product;
        init();
    }
    private void init(){
        lblName.setText(product.getStyle().getName()+"/"+product.getColor().getName()+"/"+product.getSize().getName());
        lblBrand.setText(product.getBrand().getName());
        lblPrice.setText(Utilities.moneda.format(product.getPresentationDefault().getPriceDefault().getPrice()));
        lblSize.setText(product.getSize().getName());
        lblIcon.setIcon(product.getIcons().get(0));
    }
    public JPanel getContentPane(){
        return contentPane;
    }
}
