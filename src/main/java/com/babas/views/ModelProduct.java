package com.babas.views;

import com.babas.App;
import com.babas.custom.CustomPane;
import com.babas.custom.ImageSlide;
import com.babas.custom.JPanelGradiente;
import com.babas.models.Product;
import com.babas.utilities.Utilities;
import com.babas.views.dialogs.DProductCatalogue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModelProduct {
    private JPanel contentPane;
    private JLabel lblPrice;
    private JLabel lblBrand;
    private JLabel lblSize;
    private JLabel lblIcon;
    private JButton btnShowDetails;
    private JTextArea lblArea;
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
        lblArea.setText(product.getStyle().getName()+"/ "+product.getColor().getName()+"/ "+product.getSize().getName());
        lblBrand.setText(product.getBrand().getName());
        lblPrice.setText(Utilities.moneda.format(product.getPresentationDefault().getPriceDefault().getPrice()));
        lblSize.setText(product.getSize().getName());
        if(!product.getIconsx200().isEmpty()){
            lblIcon.setIcon(product.getIconsx200().get(0));
        }
    }
    public JPanel getContentPane(){
        contentPane.updateUI();
        return contentPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        contentPane=new JPanelGradiente(new Color(0xC6FFDD),new Color(0xFBD786),new Color(0xF7797D));
    }
}
