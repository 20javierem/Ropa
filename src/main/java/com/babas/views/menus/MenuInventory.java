package com.babas.views.menus;

import com.babas.custom.CustomPane;
import com.babas.custom.TabbedPane;
import com.babas.utilities.Utilities;
import com.babas.views.tabs.TabProducts;
import com.formdev.flatlaf.extras.components.FlatToggleButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuInventory {
    private JPanel contentPane;
    private FlatToggleButton btnStockProducts;
    private FlatToggleButton btnProducts;
    private TabbedPane tabbedPane;
    private TabProducts tabProducts;
    public MenuInventory(TabbedPane tabbedPane){
        this.tabbedPane = tabbedPane;
        btnProducts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProducts();
            }
        });
    }
    public void loadProducts(){
        Utilities.despintarButton(btnStockProducts);
        Utilities.buttonSelected(btnProducts);
        if (tabProducts == null) {
            tabProducts = new TabProducts();
        }
        if (tabbedPane.indexOfTab(tabProducts.getTabPane().getTitle())==-1) {
            tabProducts = new TabProducts();
            tabProducts.getTabPane().setOption(btnProducts);
            tabbedPane.addTab(tabProducts.getTabPane().getTitle(), tabProducts.getTabPane().getIcon(), tabProducts.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabProducts.getTabPane().getTitle()));
    }
    public JPanel getContentPane() {
        contentPane.updateUI();
        btnProducts.updateUI();
        btnStockProducts.updateUI();
        return contentPane;
    }

    public JToggleButton getBtnStockProducts() {
        return btnStockProducts;
    }

    public JToggleButton getBtnProducts() {
        return btnProducts;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        contentPane =new CustomPane();
    }
}
