package com.babas.views.menus;

import com.babas.custom.CustomPane;
import com.babas.custom.TabbedPane;
import com.babas.utilities.Utilities;
import com.babas.views.tabs.TabBranchs;
import com.babas.views.tabs.TabNewRental;
import com.babas.views.tabs.TabNewReserve;
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
    private TabNewReserve tabNewReserve;

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

            }
        });
    }
    public void loadNewReserve(){
        Utilities.despintarButton(btnRecordReserves);
        Utilities.buttonSelected(btnNewReserve);
        if (tabNewReserve == null) {
            tabNewReserve = new TabNewReserve();
        }
        if (tabbedPane.indexOfTab(tabNewReserve.getTabPane().getTitle())==-1) {
            tabNewReserve = new TabNewReserve();
            tabNewReserve.getTabPane().setOption(btnNewReserve);
            tabbedPane.addTab(tabNewReserve.getTabPane().getTitle(), tabNewReserve.getTabPane().getIcon(), tabNewReserve.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabNewReserve.getTabPane().getTitle()));
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