package com.babas.views.menus;

import com.babas.custom.CustomPane;
import com.babas.custom.TabbedPane;
import com.babas.utilities.Utilities;
import com.babas.views.tabs.TabCatalogue;
import com.babas.views.tabs.TabNewSale;
import com.babas.views.tabs.TabRecordSales;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.components.FlatToggleButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuSales {
    private JPanel contentPane;
    private FlatToggleButton btnNewSale;
    private FlatToggleButton btnRecordSales;
    private FlatToggleButton btnCatalogue;
    private TabbedPane tabbedPane;
    private TabNewSale tabNewSale;
    private TabRecordSales tabRecordSales;
    private TabCatalogue tabCatalogue;

    public MenuSales(TabbedPane tabbedPane){
        this.tabbedPane=tabbedPane;
        btnNewSale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewSale();
            }
        });
        btnRecordSales.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRecordSales();
            }
        });
        btnCatalogue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadCatalogue();
            }
        });
    }

    public void loadCatalogue(){
        Utilities.despintarButton(btnRecordSales);
        Utilities.despintarButton(btnNewSale);
        Utilities.buttonSelected(btnCatalogue);
        if (tabCatalogue == null) {
            tabCatalogue = new TabCatalogue();
        }
        if (tabbedPane.indexOfTab(tabCatalogue.getTabPane().getTitle())==-1) {
            tabCatalogue = new TabCatalogue();
            tabCatalogue.getTabPane().setOption(btnCatalogue);
            tabbedPane.addTab(tabCatalogue.getTabPane().getTitle(), tabCatalogue.getTabPane().getIcon(), tabCatalogue.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabCatalogue.getTabPane().getTitle()));
    }

    public void loadNewSale(){
        Utilities.despintarButton(btnRecordSales);
        Utilities.despintarButton(btnCatalogue);
        Utilities.buttonSelected(btnNewSale);
        if (tabNewSale == null) {
            tabNewSale = new TabNewSale();
        }
        if (tabbedPane.indexOfTab(tabNewSale.getTabPane().getTitle())==-1) {
            tabNewSale = new TabNewSale();
            tabNewSale.getTabPane().setOption(btnNewSale);
            tabbedPane.addTab(tabNewSale.getTabPane().getTitle(), tabNewSale.getTabPane().getIcon(), tabNewSale.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabNewSale.getTabPane().getTitle()));
    }
    public void loadRecordSales(){
        Utilities.despintarButton(btnNewSale);
        Utilities.despintarButton(btnCatalogue);
        Utilities.buttonSelected(btnRecordSales);
        if (tabRecordSales == null) {
            tabRecordSales = new TabRecordSales();
        }
        if (tabbedPane.indexOfTab(tabRecordSales.getTabPane().getTitle())==-1) {
            tabRecordSales = new TabRecordSales();
            tabRecordSales.getTabPane().setOption(btnRecordSales);
            tabbedPane.addTab(tabRecordSales.getTabPane().getTitle(), tabRecordSales.getTabPane().getIcon(), tabRecordSales.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabRecordSales.getTabPane().getTitle()));
    }
    public JPanel getContentPane() {
        contentPane.updateUI();
        btnCatalogue.updateUI();
        btnRecordSales.updateUI();
        btnNewSale.updateUI();
        return contentPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        contentPane=new CustomPane();
    }
}
