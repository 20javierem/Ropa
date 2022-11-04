package com.babas.views;

import com.babas.App;
import com.babas.custom.CustomPane;
import com.babas.custom.ImageSlide;
import com.babas.custom.JPanelGradiente;
import com.babas.models.Product;
import com.babas.utilities.Utilities;
import com.babas.views.dialogs.DProductCatalogue;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;

public class ModelProduct {
    private JPanel contentPane;
    private JLabel lblPrice;
    private JLabel lblBrand;
    private JLabel lblSize;
    private JLabel lblIcon;
    private JTextArea lblArea;
    private JLabel lblNamePrice;
    private Product product;

    public ModelProduct(Product product) {
        this.product = product;
        init();
        contentPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DProductCatalogue dProductCatalogue = new DProductCatalogue(product);
                dProductCatalogue.setVisible(true);
            }
        });
        lblArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DProductCatalogue dProductCatalogue = new DProductCatalogue(product);
                dProductCatalogue.setVisible(true);
            }
        });
    }

    private void init() {
        lblArea.setText(product.getStyle().getName());
        lblBrand.setText(product.getBrand().getName());
        lblPrice.setText(Utilities.moneda.format(product.getPresentationDefault().getPriceDefault().getPrice()));
        lblSize.setText(product.getSize().getName());
        lblNamePrice.setText(product.getPresentationDefault().getName() + ":");
        if (!product.getIconsx200(false).isEmpty()) {
            if (product.getIconsx200(false).get(0) != null) {
                lblIcon.setIcon(product.getIconsx200(false).get(0));
            }
        }
        loadCursors();
    }
    private void loadCursors(){
        contentPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblArea.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    public JPanel getContentPane() {
        contentPane.updateUI();
        return contentPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        contentPane = new JPanelGradiente(new Color(0xC6FFDD), new Color(0xFBD786), new Color(0xF7797D));
    }

}
