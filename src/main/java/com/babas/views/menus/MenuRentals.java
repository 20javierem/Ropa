package com.babas.views.menus;

import com.babas.custom.CustomPane;
import com.babas.custom.TabbedPane;
import com.babas.models.Rental;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.views.tabs.TabNewRental;
import com.babas.views.tabs.TabNewSale;
import com.babas.views.tabs.TabRecordRentals;
import com.babas.views.tabs.TabRentalsActives;
import com.formdev.flatlaf.extras.components.FlatToggleButton;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuRentals {
    private JPanel contentPane;
    private FlatToggleButton btnNewRental;
    private FlatToggleButton btnRecordRentals;
    private FlatToggleButton btnRentalsActives;
    private TabbedPane tabbedPane;
    private TabNewRental tabNewRental;
    private TabRecordRentals tabRecordRentals;
    private TabRentalsActives tabRentalsActives;

    public MenuRentals(TabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        $$$setupUI$$$();
        btnNewRental.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewRental(new Rental());
            }
        });
        btnRecordRentals.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRecordRentals();
            }
        });
        btnRentalsActives.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRentalsActives();
            }
        });
    }

    public void loadNewRental(Rental rental) {
        btnNewRental.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Utilities.despintarButton(btnRecordRentals);
        Utilities.despintarButton(btnRentalsActives);
        Utilities.buttonSelected(btnNewRental);
        if (tabNewRental == null) {
            tabNewRental = new TabNewRental(rental);
        }
        if (tabbedPane.indexOfTab(tabNewRental.getTabPane().getTitle()) == -1) {
            tabNewRental = new TabNewRental(rental);
            tabNewRental.getTabPane().setOption(btnNewRental);
            tabbedPane.addTab(tabNewRental.getTabPane().getTitle(), tabNewRental.getTabPane().getIcon(), tabNewRental.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabNewRental.getTabPane().getTitle()));
        btnNewRental.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void loadRentalsActives() {
        btnRentalsActives.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Utilities.despintarButton(btnNewRental);
        Utilities.despintarButton(btnRecordRentals);
        Utilities.buttonSelected(btnRentalsActives);
        if (tabRentalsActives == null) {
            tabRentalsActives = new TabRentalsActives();
        }
        if (tabbedPane.indexOfTab(tabRentalsActives.getTabPane().getTitle()) == -1) {
            tabRentalsActives = new TabRentalsActives();
            tabRentalsActives.getTabPane().setOption(btnRentalsActives);
            tabbedPane.addTab(tabRentalsActives.getTabPane().getTitle(), tabRentalsActives.getTabPane().getIcon(), tabRentalsActives.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabRentalsActives.getTabPane().getTitle()));
        btnRentalsActives.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void loadRecordRentals() {
        btnRecordRentals.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Utilities.despintarButton(btnNewRental);
        Utilities.despintarButton(btnRentalsActives);
        Utilities.buttonSelected(btnRecordRentals);
        if (tabRecordRentals == null) {
            tabRecordRentals = new TabRecordRentals();
        }
        if (tabbedPane.indexOfTab(tabRecordRentals.getTabPane().getTitle()) == -1) {
            tabRecordRentals = new TabRecordRentals();
            tabRecordRentals.getTabPane().setOption(btnRecordRentals);
            tabbedPane.addTab(tabRecordRentals.getTabPane().getTitle(), tabRecordRentals.getTabPane().getIcon(), tabRecordRentals.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabRecordRentals.getTabPane().getTitle()));
        btnRecordRentals.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public JPanel getContentPane() {
        if (Babas.user.isGroupDefault()) {
            btnNewRental.setEnabled(Babas.user.getPermissionGroup().isNewRental());
            btnRecordRentals.setEnabled(Babas.user.getPermissionGroup().isRecordRentals());
            btnRentalsActives.setEnabled(Babas.user.getPermissionGroup().isRentalsActives());
        } else {
            btnNewRental.setEnabled(Babas.user.getPermitions().isNewRental());
            btnRecordRentals.setEnabled(Babas.user.getPermitions().isRecordRentals());
            btnRentalsActives.setEnabled(Babas.user.getPermitions().isRentalsActives());
        }
        contentPane.updateUI();
        btnNewRental.updateUI();
        btnRecordRentals.updateUI();
        btnRentalsActives.updateUI();
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
        contentPane.setLayout(new GridLayoutManager(4, 1, new Insets(10, 5, 5, 5), -1, 10));
        contentPane.setMaximumSize(new Dimension(130, 2147483647));
        contentPane.setMinimumSize(new Dimension(130, 109));
        contentPane.setPreferredSize(new Dimension(130, 109));
        btnNewRental = new FlatToggleButton();
        btnNewRental.setText("<html><center>Nuevo alquiler<center></html>");
        contentPane.add(btnNewRental, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        btnRecordRentals = new FlatToggleButton();
        btnRecordRentals.setText("<html><center>Historial de alquileres<center></html>");
        contentPane.add(btnRecordRentals, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnRentalsActives = new FlatToggleButton();
        btnRentalsActives.setText("<html><center>Alquileres activos<center></html>");
        contentPane.add(btnRentalsActives, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
