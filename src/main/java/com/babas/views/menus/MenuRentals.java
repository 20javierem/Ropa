package com.babas.views.menus;

import com.babas.custom.CustomPane;
import com.babas.custom.TabbedPane;
import com.formdev.flatlaf.extras.components.FlatToggleButton;

import javax.swing.*;

public class MenuRentals {
    private JPanel contentPane;
    private FlatToggleButton btnNewRental;
    private FlatToggleButton btnRecordRentals;
    private TabbedPane tabbedPane;

    public MenuRentals(TabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    public JPanel getContentPane() {
        contentPane.updateUI();
        btnNewRental.updateUI();
        btnRecordRentals.updateUI();
        return contentPane;
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        contentPane =new CustomPane();
    }
}
