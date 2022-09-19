package com.babas.views.tabs;

import com.babas.custom.TabPane;
import com.babas.models.Product;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorProduct;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorSize;
import com.babas.utilitiesTables.tablesCellRendered.ProductCellRendered;
import com.babas.utilitiesTables.tablesModels.ProductAbstractModel;
import com.babas.views.dialogs.*;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.formdev.flatlaf.icons.FlatSearchIcon;

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
    private JButton btnAllSexs;
    private JButton btnBrands;
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
        btnAllSexs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSexs();
            }
        });
        btnBrands.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBrands();
            }
        });
    }
    private void loadBrands(){
        DAllBrands dAllBrands=new DAllBrands();
        dAllBrands.setVisible(true);
    }
    private void loadSexs(){
        DAllSexs dAllSexs=new DAllSexs();
        dAllSexs.setVisible(true);
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
        loadIcons();
        getTabPane().getActions().addActionListener(e -> model.fireTableDataChanged());
        loadTable();
    }
    private void loadIcons(){
        flatTextField.setLeadingIcon(new FlatSearchIcon());
    }
    private void loadTable(){
        model=new ProductAbstractModel(FPrincipal.products);
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        ProductCellRendered.setCellRenderer(table,null);
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorProduct("delete"));
        table.getColumnModel().getColumn(model.getColumnCount() - 2).setCellEditor(new JButtonEditorProduct("edit"));
        table.getColumnModel().getColumn(model.getColumnCount() - 3).setCellEditor(new JButtonEditorProduct("images"));
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
