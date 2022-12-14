package com.babas.views.menus;

import com.babas.custom.CustomPane;
import com.babas.custom.TabbedPane;
import com.babas.models.Sale;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.views.tabs.TabCatalogue;
import com.babas.views.tabs.TabNewSale;
import com.babas.views.tabs.TabRecordSales;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.components.FlatToggleButton;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
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

    public MenuSales(TabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        $$$setupUI$$$();
        btnNewSale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewSale(true, new Sale());
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

    public void loadCatalogue() {
        btnCatalogue.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Utilities.despintarButton(btnRecordSales);
        Utilities.despintarButton(btnNewSale);
        Utilities.buttonSelected(btnCatalogue);
        if (tabCatalogue == null) {
            tabCatalogue = new TabCatalogue();
        }
        if (tabbedPane.indexOfTab(tabCatalogue.getTabPane().getTitle()) == -1) {
            tabCatalogue = new TabCatalogue();
            tabCatalogue.getTabPane().setOption(btnCatalogue);
            tabbedPane.addTab(tabCatalogue.getTabPane().getTitle(), tabCatalogue.getTabPane().getIcon(), tabCatalogue.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabCatalogue.getTabPane().getTitle()));
        btnCatalogue.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void loadNewSale(boolean selected, Sale sale) {
        btnNewSale.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Utilities.despintarButton(btnRecordSales);
        Utilities.despintarButton(btnCatalogue);
        Utilities.buttonSelected(btnNewSale);
        if (tabNewSale == null) {
            tabNewSale = new TabNewSale(sale);
        }
        if (tabbedPane.indexOfTab(tabNewSale.getTabPane().getTitle()) == -1) {
            tabNewSale = new TabNewSale(sale);
            tabNewSale.getTabPane().setOption(btnNewSale);
            tabbedPane.addTab(tabNewSale.getTabPane().getTitle(), tabNewSale.getTabPane().getIcon(), tabNewSale.getTabPane());
        }
        if (selected) {
            tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabNewSale.getTabPane().getTitle()));
        } else {
            loadCatalogue();
        }
        btnNewSale.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void loadRecordSales() {
        btnRecordSales.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Utilities.despintarButton(btnNewSale);
        Utilities.despintarButton(btnCatalogue);
        Utilities.buttonSelected(btnRecordSales);
        if (tabRecordSales == null) {
            tabRecordSales = new TabRecordSales();
        }
        if (tabbedPane.indexOfTab(tabRecordSales.getTabPane().getTitle()) == -1) {
            tabRecordSales = new TabRecordSales();
            tabRecordSales.getTabPane().setOption(btnRecordSales);
            tabbedPane.addTab(tabRecordSales.getTabPane().getTitle(), tabRecordSales.getTabPane().getIcon(), tabRecordSales.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabRecordSales.getTabPane().getTitle()));
        btnRecordSales.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public JPanel getContentPane() {
        if (Babas.user.isGroupDefault()) {
            btnCatalogue.setEnabled(Babas.user.getPermissionGroup().isShowCatalogue());
            btnRecordSales.setEnabled(Babas.user.getPermissionGroup().isRecordSales());
            btnNewSale.setEnabled(Babas.user.getPermissionGroup().isNewSale());
        } else {
            btnCatalogue.setEnabled(Babas.user.getPermitions().isShowCatalogue());
            btnRecordSales.setEnabled(Babas.user.getPermitions().isRecordSales());
            btnNewSale.setEnabled(Babas.user.getPermitions().isNewSale());
        }
        contentPane.updateUI();
        btnCatalogue.updateUI();
        btnRecordSales.updateUI();
        btnNewSale.updateUI();
        return contentPane;
    }

    public TabNewSale getTabNewSale() {
        return tabNewSale;
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
        contentPane.setLayout(new GridLayoutManager(4, 1, new Insets(10, 5, 5, 5), -1, 10));
        contentPane.setMaximumSize(new Dimension(130, 2147483647));
        contentPane.setMinimumSize(new Dimension(130, 109));
        contentPane.setPreferredSize(new Dimension(130, 109));
        final Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        btnNewSale = new FlatToggleButton();
        btnNewSale.setText("<html><center>Nueva venta<center></html>");
        contentPane.add(btnNewSale, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnRecordSales = new FlatToggleButton();
        btnRecordSales.setText("<html><center>Historial de ventas<center></html>");
        contentPane.add(btnRecordSales, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnCatalogue = new FlatToggleButton();
        btnCatalogue.setText("<html><center>Ver cat??logo<center></html>");
        contentPane.add(btnCatalogue, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
