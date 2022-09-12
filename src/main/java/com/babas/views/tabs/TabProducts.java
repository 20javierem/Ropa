package com.babas.views.tabs;

import com.babas.custom.TabPane;
import com.babas.models.Product;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.tablesModels.ProductAbstractModel;
import com.babas.views.dialogs.*;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TabProducts {
    private TabPane tabPane;
    private JButton btnClearFilters;
    private FlatTable table;
    private FlatTextField flatTextField;
    private JButton btnCategorys;
    private JButton btnStyles;
    private JButton btnSizes;
    private JButton btnColors;
    private JButton btnNewProduct;
    private ProductAbstractModel model;

    public TabProducts(){
        init();
        btnSizes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btnColors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadColors();
            }
        });
        btnCategorys.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadCategorys();
            }
        });
        btnSizes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSizes();
            }
        });
        btnNewProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewProduct();
            }
        });
    }
    private void loadNewProduct(){
        DProduct dProduct=new DProduct(new Product());
        dProduct.setVisible(true);
    }
    private void loadColors(){
        DAllColors dAllColors=new DAllColors();
        dAllColors.setVisible(true);
    }
    private void loadSizes(){
        DAllSizes dAllSizes=new DAllSizes();
        dAllSizes.setVisible(true);
    }
    private void loadCategorys(){
        DAllCategorys dAllCategorys=new DAllCategorys();
        dAllCategorys.setVisible(true);
    }
    private void init(){
        tabPane.setTitle("Productos");
        loadTable();
    }
    private void loadTable(){
        model=new ProductAbstractModel(FPrincipal.products);
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
    }
    private void filter(){

    }
    public TabPane getTabPane() {
        return tabPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
