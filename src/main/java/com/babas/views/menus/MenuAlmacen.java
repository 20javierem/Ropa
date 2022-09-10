package com.babas.views.menus;

import com.babas.custom.CustomPane;
import com.babas.custom.TabbedPane;
import com.babas.utilities.Utilities;
import com.formdev.flatlaf.FlatLaf;

import javax.swing.*;

public class MenuAlmacen {
    private JPanel contentPane;
    private JToggleButton btnInventory;
    private JToggleButton btnProducts;
    private JToggleButton btnStockBranchs;
    private JToggleButton btnNewTransfer;
    private JToggleButton btnRecordTransfers;
    private TabbedPane tabbedPane;

    public MenuAlmacen(TabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    public JPanel getContentPane() {
        contentPane.updateUI();
        btnInventory.updateUI();
        btnProducts.updateUI();
        btnStockBranchs.updateUI();
        btnNewTransfer.updateUI();
        btnRecordTransfers.updateUI();
        return contentPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        contentPane=new CustomPane();
    }
}
