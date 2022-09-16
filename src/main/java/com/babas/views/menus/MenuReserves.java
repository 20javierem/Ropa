package com.babas.views.menus;

import com.babas.custom.CustomPane;
import com.babas.custom.TabbedPane;
import com.babas.utilities.Utilities;
import com.babas.views.tabs.TabBranchs;
import com.babas.views.tabs.TabUsers;
import com.formdev.flatlaf.extras.components.FlatToggleButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuReserves {
    private FlatToggleButton btnNewReserve;
    private FlatToggleButton btnRecordReserves;
    private JPanel contentPane;
    private TabbedPane tabbedPane;
    private TabUsers tabUsers;
    private TabBranchs tabBranchs;

    public MenuReserves(TabbedPane tabbedPane){
        this.tabbedPane=tabbedPane;
        btnNewReserve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewReserve();
            }
        });
        btnRecordReserves.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRecordReserves();
            }
        });
    }
    public void loadNewReserve(){
        Utilities.despintarButton(btnRecordReserves);
        Utilities.buttonSelected(btnNewReserve);
        if (tabUsers == null) {
            tabUsers = new TabUsers();
        }
        if (tabbedPane.indexOfTab(tabUsers.getTabPane().getTitle())==-1) {
            tabUsers = new TabUsers();
            tabUsers.getTabPane().setOption(btnNewReserve);
            tabbedPane.addTab(tabUsers.getTabPane().getTitle(), tabUsers.getTabPane().getIcon(), tabUsers.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabUsers.getTabPane().getTitle()));
    }
    public void loadRecordReserves(){
        Utilities.despintarButton(btnNewReserve);
        Utilities.buttonSelected(btnRecordReserves);
        if (tabBranchs == null) {
            tabBranchs = new TabBranchs();
        }
        if (tabbedPane.indexOfTab(tabBranchs.getTabPane().getTitle())==-1) {
            tabBranchs = new TabBranchs();
            tabBranchs.getTabPane().setOption(btnRecordReserves);
            tabbedPane.addTab(tabBranchs.getTabPane().getTitle(), tabBranchs.getTabPane().getIcon(), tabBranchs.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabBranchs.getTabPane().getTitle()));
    }
    public JPanel getContentPane() {
        contentPane.updateUI();
        btnRecordReserves.updateUI();
        btnNewReserve.updateUI();
        return contentPane;
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        contentPane =new CustomPane();
    }
}