package com.babas.views.menus;

import com.babas.custom.CustomPane;
import com.babas.custom.TabbedPane;
import com.babas.utilities.Utilities;
import com.babas.views.tabs.TabNewRental;
import com.babas.views.tabs.TabNewSale;
import com.formdev.flatlaf.extras.components.FlatToggleButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuRentals {
    private JPanel contentPane;
    private FlatToggleButton btnNewRental;
    private FlatToggleButton btnRecordRentals;
    private TabbedPane tabbedPane;
    private TabNewRental tabNewRental;

    public MenuRentals(TabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        btnNewRental.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewRental();
            }
        });
    }

    public void loadNewRental(){
        Utilities.despintarButton(btnRecordRentals);
        Utilities.buttonSelected(btnNewRental);
        if (tabNewRental == null) {
            tabNewRental = new TabNewRental();
        }
        if (tabbedPane.indexOfTab(tabNewRental.getTabPane().getTitle())==-1) {
            tabNewRental = new TabNewRental();
            tabNewRental.getTabPane().setOption(btnNewRental);
            tabbedPane.addTab(tabNewRental.getTabPane().getTitle(), tabNewRental.getTabPane().getIcon(), tabNewRental.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabNewRental.getTabPane().getTitle()));
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
