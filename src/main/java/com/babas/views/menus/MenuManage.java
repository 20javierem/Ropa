package com.babas.views.menus;

import com.babas.custom.CustomPane;
import com.babas.custom.TabbedPane;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.views.tabs.TabBranchs;
import com.babas.views.tabs.TabClients;
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
    private FlatToggleButton btnClients;
    private TabbedPane tabbedPane;
    private TabUsers tabUsers;
    private TabBranchs tabBranchs;
    private TabProducts tabProducts;
    private TabClients tabClients;

    public MenuManage(TabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        $$$setupUI$$$();
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
        btnClients.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadClients();
            }
        });
    }

    public void loadProducts() {
        btnProducts.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Utilities.despintarButton(btnUsers);
        Utilities.despintarButton(btnBranchs);
        Utilities.despintarButton(btnClients);
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
        btnProducts.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void loadUsers() {
        btnUsers.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Utilities.despintarButton(btnProducts);
        Utilities.despintarButton(btnBranchs);
        Utilities.despintarButton(btnClients);
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
        btnUsers.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void loadClients() {
        btnClients.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Utilities.despintarButton(btnProducts);
        Utilities.despintarButton(btnBranchs);
        Utilities.despintarButton(btnUsers);
        Utilities.buttonSelected(btnClients);
        if (tabClients == null) {
            tabClients = new TabClients();
        }
        if (tabbedPane.indexOfTab(tabClients.getTabPane().getTitle()) == -1) {
            tabClients = new TabClients();
            tabClients.getTabPane().setOption(btnClients);
            tabbedPane.addTab(tabClients.getTabPane().getTitle(), tabClients.getTabPane().getIcon(), tabClients.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabClients.getTabPane().getTitle()));
        btnClients.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void loadBranchs() {
        btnBranchs.setCursor(new Cursor(Cursor.WAIT_CURSOR));
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
        btnBranchs.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public JPanel getContentPane() {
        if (Babas.user.isGroupDefault()) {
            btnProducts.setEnabled(Babas.user.getPermissionGroup().isManageProducts());
            btnBranchs.setEnabled(Babas.user.getPermissionGroup().isManageBranchs());
            btnUsers.setEnabled(Babas.user.getPermissionGroup().isManageUsers());
            btnClients.setEnabled(Babas.user.getPermissionGroup().isManageClients());
        } else {
            btnProducts.setEnabled(Babas.user.getPermitions().isManageProducts());
            btnBranchs.setEnabled(Babas.user.getPermitions().isManageBranchs());
            btnUsers.setEnabled(Babas.user.getPermitions().isManageUsers());
            btnClients.setEnabled(Babas.user.getPermitions().isManageClients());
        }
        contentPane.updateUI();
        btnProducts.updateUI();
        btnBranchs.updateUI();
        btnClients.updateUI();
        btnUsers.updateUI();
        return contentPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        contentPane = new CustomPane();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane.setLayout(new GridLayoutManager(5, 1, new Insets(10, 5, 5, 5), -1, 10));
        contentPane.setMaximumSize(new Dimension(130, 2147483647));
        contentPane.setMinimumSize(new Dimension(130, 109));
        contentPane.setPreferredSize(new Dimension(130, 109));
        final Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        btnUsers = new FlatToggleButton();
        btnUsers.setText("Usuarios");
        contentPane.add(btnUsers, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnBranchs = new FlatToggleButton();
        btnBranchs.setText("Sucursales");
        contentPane.add(btnBranchs, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnProducts = new FlatToggleButton();
        btnProducts.setText("<html><center>Productos<center></html>");
        contentPane.add(btnProducts, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnClients = new FlatToggleButton();
        btnClients.setText("<html><center>Clientes<center></html>");
        contentPane.add(btnClients, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
