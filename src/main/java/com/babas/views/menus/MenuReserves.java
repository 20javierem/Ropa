package com.babas.views.menus;

import com.babas.custom.CustomPane;
import com.babas.custom.TabbedPane;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.views.tabs.TabNewReserve;
import com.babas.views.tabs.TabRecordRentals;
import com.babas.views.tabs.TabRecordReserves;
import com.babas.views.tabs.TabReservesActives;
import com.formdev.flatlaf.extras.components.FlatToggleButton;

import javax.swing.*;
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
    public void loadRecordRentals(){
        Utilities.despintarButton(btnNewReserve);
        Utilities.despintarButton(btnReservesActives);
        Utilities.buttonSelected(btnRecordReserves);
        if (tabRecordReserves == null) {
            tabRecordReserves = new TabRecordReserves();
        }
        if (tabbedPane.indexOfTab(tabRecordReserves.getTabPane().getTitle())==-1) {
            tabRecordReserves = new TabRecordReserves();
            tabRecordReserves.getTabPane().setOption(btnRecordReserves);
            tabbedPane.addTab(tabRecordReserves.getTabPane().getTitle(), tabRecordReserves.getTabPane().getIcon(), tabRecordReserves.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabRecordReserves.getTabPane().getTitle()));
    }
    public void loadReservesActives(){
        Utilities.despintarButton(btnNewReserve);
        Utilities.despintarButton(btnRecordReserves);
        Utilities.buttonSelected(btnReservesActives);
        if (tabReservesActives == null) {
            tabReservesActives = new TabReservesActives();
        }
        if (tabbedPane.indexOfTab(tabReservesActives.getTabPane().getTitle())==-1) {
            tabReservesActives = new TabReservesActives();
            tabReservesActives.getTabPane().setOption(btnReservesActives);
            tabbedPane.addTab(tabReservesActives.getTabPane().getTitle(), tabReservesActives.getTabPane().getIcon(), tabReservesActives.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabReservesActives.getTabPane().getTitle()));
    }
    public JPanel getContentPane() {
        btnRecordReserves.setEnabled(Babas.user.getPermitions().isRecordReserves());
        btnNewReserve.setEnabled(Babas.user.getPermitions().isNewReserve());
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