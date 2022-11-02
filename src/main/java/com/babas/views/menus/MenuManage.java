package com.babas.views.menus;

import com.babas.custom.CustomPane;
import com.babas.custom.TabbedPane;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.views.tabs.TabBranchs;
import com.babas.views.tabs.TabProducts;
import com.babas.views.tabs.TabUsers;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.components.FlatToggleButton;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuManage {
    private FlatToggleButton btnUsers;
    private FlatToggleButton btnBranchs;
    private JPanel contentPane;
    private FlatToggleButton btnProducts;
    private TabbedPane tabbedPane;
    private TabUsers tabUsers;
    private TabBranchs tabBranchs;
    private TabProducts tabProducts;

    public MenuManage(TabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
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
        btnProducts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProducts();
            }
        });
    }

    public void loadProducts() {
        Utilities.despintarButton(btnUsers);
        Utilities.despintarButton(btnBranchs);
        Utilities.buttonSelected(btnProducts);
        if (tabProducts == null) {
            tabProducts = new TabProducts();
        }
        if (tabbedPane.indexOfTab(tabProducts.getTabPane().getTitle()) == -1) {
            tabProducts = new TabProducts();
            tabProducts.getTabPane().setOption(btnProducts);
            tabbedPane.addTab(tabProducts.getTabPane().getTitle(), tabProducts.getTabPane().getIcon(), tabProducts.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabProducts.getTabPane().getTitle()));
    }

    public void loadUsers() {
        Utilities.despintarButton(btnProducts);
        Utilities.despintarButton(btnBranchs);
        Utilities.buttonSelected(btnUsers);
        if (tabUsers == null) {
            tabUsers = new TabUsers();
        }
        if (tabbedPane.indexOfTab(tabUsers.getTabPane().getTitle()) == -1) {
            tabUsers = new TabUsers();
            tabUsers.getTabPane().setOption(btnUsers);
            tabbedPane.addTab(tabUsers.getTabPane().getTitle(), tabUsers.getTabPane().getIcon(), tabUsers.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabUsers.getTabPane().getTitle()));
    }

    public void loadBranchs() {
        Utilities.despintarButton(btnProducts);
        Utilities.despintarButton(btnUsers);
        Utilities.buttonSelected(btnBranchs);
        if (tabBranchs == null) {
            tabBranchs = new TabBranchs();
        }
        if (tabbedPane.indexOfTab(tabBranchs.getTabPane().getTitle()) == -1) {
            tabBranchs = new TabBranchs();
            tabBranchs.getTabPane().setOption(btnBranchs);
            tabbedPane.addTab(tabBranchs.getTabPane().getTitle(), tabBranchs.getTabPane().getIcon(), tabBranchs.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabBranchs.getTabPane().getTitle()));
    }

    public JPanel getContentPane() {
        btnProducts.setEnabled(Babas.user.getPermitions().isManageProducts());
        btnBranchs.setEnabled(Babas.user.getPermitions().isManageBranchs());
        btnUsers.setEnabled(Babas.user.getPermitions().isManageUsers());
        contentPane.updateUI();
        btnProducts.updateUI();
        btnBranchs.updateUI();
        btnUsers.updateUI();
        return contentPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        contentPane = new CustomPane();
    }

}
