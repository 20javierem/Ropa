package com.babas.views;

import com.babas.custom.ImageSlide;
import com.babas.models.Product;
import com.babas.utilities.Utilities;
import com.babas.views.dialogs.DProductCatalogue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModelProduct {
    private JPanel contentPane;
    private JLabel lblName;
    private JLabel lblPrice;
    private JLabel lblBrand;
    private JLabel lblSize;
    private JLabel lblIcon;
    private JButton btnShowDetails;
    private Product product;

    public ModelProduct(Product product) {
        this.product = product;
        init();
        btnShowDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DProductCatalogue dProductCatalogue=new DProductCatalogue(product);
                dProductCatalogue.setVisible(true);
            }
        });
    }
    private void init(){
        lblName.setText(product.getStyle().getName()+"/"+product.getColor().getName()+"/"+product.getSize().getName());
        lblBrand.setText(product.getBrand().getName());
        lblPrice.setText(Utilities.moneda.format(product.getPresentationDefault().getPriceDefault().getPrice()));
        lblSize.setText(product.getSize().getName());
        lblIcon.setIcon(product.getIcons().get(0));
        lblIcon.setIcon(new ImageIcon(ImageSlide.toImage(product.getIcons().get(0)).getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
    }
    public JPanel getContentPane(){
        return contentPane;
    }
}
