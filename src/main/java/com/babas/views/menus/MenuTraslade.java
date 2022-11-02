package com.babas.views.menus;

import com.babas.custom.CustomPane;
import com.babas.custom.TabbedPane;
import com.babas.models.Transfer;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.views.tabs.TabNewTraslade;
import com.babas.views.tabs.TabRecordTransfers;
import com.formdev.flatlaf.extras.components.FlatToggleButton;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuTraslade {
    private FlatToggleButton btnNewTraslade;
    private FlatToggleButton btnRecordTraslades;
    private JPanel contentPane;
    private TabbedPane tabbedPane;
    private TabRecordTransfers tabRecordTransfers;
    private TabNewTraslade tabNewTraslade;

    public MenuTraslade(TabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        btnNewTraslade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewTraslade();
            }
        });
        btnRecordTraslades.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRecordTraslades();
            }
        });
    }

    public void loadNewTraslade() {
        Utilities.despintarButton(btnRecordTraslades);
        Utilities.buttonSelected(btnNewTraslade);
        if (tabNewTraslade == null) {
            tabNewTraslade = new TabNewTraslade(new Transfer());
        }
        if (tabbedPane.indexOfTab(tabNewTraslade.getTabPane().getTitle()) == -1) {
            tabNewTraslade = new TabNewTraslade(new Transfer());
            tabNewTraslade.getTabPane().setOption(btnNewTraslade);
            tabbedPane.addTab(tabNewTraslade.getTabPane().getTitle(), tabNewTraslade.getTabPane().getIcon(), tabNewTraslade.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabNewTraslade.getTabPane().getTitle()));
    }

    public void loadRecordTraslades() {
        Utilities.despintarButton(btnNewTraslade);
        Utilities.buttonSelected(btnRecordTraslades);
        if (tabRecordTransfers == null) {
            tabRecordTransfers = new TabRecordTransfers();
        }
        if (tabbedPane.indexOfTab(tabRecordTransfers.getTabPane().getTitle()) == -1) {
            tabRecordTransfers = new TabRecordTransfers();
            tabRecordTransfers.getTabPane().setOption(btnRecordTraslades);
            tabbedPane.addTab(tabRecordTransfers.getTabPane().getTitle(), tabRecordTransfers.getTabPane().getIcon(), tabRecordTransfers.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabRecordTransfers.getTabPane().getTitle()));
    }

    public JPanel getContentPane() {
        btnRecordTraslades.setEnabled(Babas.user.getPermitions().isRecordTransfers());
        btnNewTraslade.setEnabled(Babas.user.getPermitions().isNewTransfer());
        contentPane.updateUI();
        btnRecordTraslades.updateUI();
        btnNewTraslade.updateUI();
        return contentPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        contentPane = new CustomPane();
    }

}
