package com.babas.views.menus;

import com.babas.custom.CustomPane;
import com.babas.custom.TabbedPane;
import com.babas.utilities.Utilities;
import com.babas.views.tabs.TabBranchs;
import com.babas.views.tabs.TabProducts;
import com.babas.views.tabs.TabUsers;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.components.FlatToggleButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuManage {
    private FlatToggleButton btnUsers;
    private FlatToggleButton btnBranchs;
    private JPanel contentPane;
    private TabbedPane tabbedPane;
    private TabUsers tabUsers;
    private TabBranchs tabBranchs;

    public MenuManage(TabbedPane tabbedPane){
        this.tabbedPane=tabbedPane;
        btnUsers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadUsers();
            }
        });
        btnBranchs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBranchs();
            }
        });
    }
    public void loadUsers(){
        Utilities.despintarButton(btnBranchs);
        Utilities.buttonSelected(btnUsers);
        if (tabUsers == null) {
            tabUsers = new TabUsers();
        }
        if (tabbedPane.indexOfTab(tabUsers.getTabPane().getTitle())==-1) {
            tabUsers = new TabUsers();
            tabUsers.getTabPane().setOption(btnUsers);
            tabbedPane.addTab(tabUsers.getTabPane().getTitle(), tabUsers.getTabPane().getIcon(), tabUsers.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabUsers.getTabPane().getTitle()));
    }
    public void loadBranchs(){
        Utilities.despintarButton(btnUsers);
        Utilities.buttonSelected(btnBranchs);
        if (tabBranchs == null) {
            tabBranchs = new TabBranchs();
        }
        if (tabbedPane.indexOfTab(tabBranchs.getTabPane().getTitle())==-1) {
            tabBranchs = new TabBranchs();
            tabBranchs.getTabPane().setOption(btnBranchs);
            tabbedPane.addTab(tabBranchs.getTabPane().getTitle(), tabBranchs.getTabPane().getIcon(), tabBranchs.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabBranchs.getTabPane().getTitle()));
    }
    public JPanel getContentPane() {
        contentPane.updateUI();
        btnBranchs.updateUI();
        btnUsers.updateUI();
        return contentPane;
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        contentPane =new CustomPane();
    }
}
