package com.babas.views.menus;

import com.babas.custom.CustomPane;
import com.babas.custom.TabbedPane;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.views.tabs.TabRecordBoxSesions;
import com.formdev.flatlaf.extras.components.FlatToggleButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuBoxes {
    private JPanel contentPane;
    private FlatToggleButton btnRecordBoxSessions;
    private TabbedPane tabbedPane;
    private TabRecordBoxSesions tabRecordBoxSesions;

    public MenuBoxes(TabbedPane tabbedPane){
        this.tabbedPane=tabbedPane;
        btnRecordBoxSessions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRecordBoxSessions();
            }
        });
    }
    public void loadRecordBoxSessions(){
        Utilities.buttonSelected(btnRecordBoxSessions);
        if (tabRecordBoxSesions == null) {
            tabRecordBoxSesions = new TabRecordBoxSesions();
        }
        if (tabbedPane.indexOfTab(tabRecordBoxSesions.getTabPane().getTitle())==-1) {
            tabRecordBoxSesions = new TabRecordBoxSesions();
            tabRecordBoxSesions.getTabPane().setOption(btnRecordBoxSessions);
            tabbedPane.addTab(tabRecordBoxSesions.getTabPane().getTitle(), tabRecordBoxSesions.getTabPane().getIcon(), tabRecordBoxSesions.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabRecordBoxSesions.getTabPane().getTitle()));
    }

    public JPanel getContentPane() {
        btnRecordBoxSessions.setEnabled(Babas.user.getPermitions().isRecordBoxes());
        contentPane.updateUI();
        btnRecordBoxSessions.updateUI();
        return contentPane;
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        contentPane =new CustomPane();
    }
}
