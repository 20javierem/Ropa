package com.babas.views.menus;

import com.babas.custom.CustomPane;
import com.babas.custom.TabbedPane;
import com.babas.models.Reserve;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.views.tabs.TabNewReserve;
import com.babas.views.tabs.TabRecordRentals;
import com.babas.views.tabs.TabRecordReserves;
import com.babas.views.tabs.TabReservesActives;
import com.formdev.flatlaf.extras.components.FlatToggleButton;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuReserves {
    private FlatToggleButton btnNewReserve;
    private FlatToggleButton btnRecordReserves;
    private JPanel contentPane;
    private FlatToggleButton btnReservesActives;
    private TabbedPane tabbedPane;
    private TabNewReserve tabNewReserve;
    private TabRecordReserves tabRecordReserves;
    private TabReservesActives tabReservesActives;

    public MenuReserves(TabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        $$$setupUI$$$();
        btnNewReserve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewReserve(new Reserve());
            }
        });
        btnRecordReserves.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRecordRentals();
            }
        });
        btnReservesActives.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadReservesActives();
            }
        });
    }

    public void loadNewReserve(Reserve reserve) {
        btnNewReserve.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Utilities.despintarButton(btnRecordReserves);
        Utilities.buttonSelected(btnNewReserve);
        if (tabNewReserve == null) {
            tabNewReserve = new TabNewReserve(reserve);
        }
        if (tabbedPane.indexOfTab(tabNewReserve.getTabPane().getTitle()) == -1) {
            tabNewReserve = new TabNewReserve(reserve);
            tabNewReserve.getTabPane().setOption(btnNewReserve);
            tabbedPane.addTab(tabNewReserve.getTabPane().getTitle(), tabNewReserve.getTabPane().getIcon(), tabNewReserve.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabNewReserve.getTabPane().getTitle()));
        btnNewReserve.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void loadRecordRentals() {
        btnRecordReserves.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Utilities.despintarButton(btnNewReserve);
        Utilities.despintarButton(btnReservesActives);
        Utilities.buttonSelected(btnRecordReserves);
        if (tabRecordReserves == null) {
            tabRecordReserves = new TabRecordReserves();
        }
        if (tabbedPane.indexOfTab(tabRecordReserves.getTabPane().getTitle()) == -1) {
            tabRecordReserves = new TabRecordReserves();
            tabRecordReserves.getTabPane().setOption(btnRecordReserves);
            tabbedPane.addTab(tabRecordReserves.getTabPane().getTitle(), tabRecordReserves.getTabPane().getIcon(), tabRecordReserves.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabRecordReserves.getTabPane().getTitle()));
        btnRecordReserves.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void loadReservesActives() {
        btnReservesActives.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Utilities.despintarButton(btnNewReserve);
        Utilities.despintarButton(btnRecordReserves);
        Utilities.buttonSelected(btnReservesActives);
        if (tabReservesActives == null) {
            tabReservesActives = new TabReservesActives();
        }
        if (tabbedPane.indexOfTab(tabReservesActives.getTabPane().getTitle()) == -1) {
            tabReservesActives = new TabReservesActives();
            tabReservesActives.getTabPane().setOption(btnReservesActives);
            tabbedPane.addTab(tabReservesActives.getTabPane().getTitle(), tabReservesActives.getTabPane().getIcon(), tabReservesActives.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabReservesActives.getTabPane().getTitle()));
        btnReservesActives.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public JPanel getContentPane() {
        if (Babas.user.isGroupDefault()) {
            btnReservesActives.setEnabled(Babas.user.getPermissionGroup().isReservesActives());
            btnRecordReserves.setEnabled(Babas.user.getPermissionGroup().isRecordReserves());
            btnNewReserve.setEnabled(Babas.user.getPermissionGroup().isNewReserve());
        } else {
            btnReservesActives.setEnabled(Babas.user.getPermitions().isReservesActives());
            btnRecordReserves.setEnabled(Babas.user.getPermitions().isRecordReserves());
            btnNewReserve.setEnabled(Babas.user.getPermitions().isNewReserve());
        }
        contentPane.updateUI();
        btnRecordReserves.updateUI();
        btnReservesActives.updateUI();
        btnNewReserve.updateUI();
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
        contentPane.setMinimumSize(new Dimension(130, 104));
        contentPane.setPreferredSize(new Dimension(130, 104));
        final Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        btnNewReserve = new FlatToggleButton();
        btnNewReserve.setText("<html><center>Nueva reserva<center></html>");
        contentPane.add(btnNewReserve, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnRecordReserves = new FlatToggleButton();
        btnRecordReserves.setText("<html><center>Historial de reservas<center></html>");
        contentPane.add(btnRecordReserves, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnReservesActives = new FlatToggleButton();
        btnReservesActives.setText("<html><center>Reservas activas<center></html>");
        contentPane.add(btnReservesActives, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}