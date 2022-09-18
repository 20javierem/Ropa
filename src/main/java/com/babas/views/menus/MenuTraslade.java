package com.babas.views.menus;

import com.babas.custom.CustomPane;
import com.babas.custom.TabbedPane;
import com.babas.models.Transfer;
import com.babas.utilities.Utilities;
import com.babas.views.dialogs.DTraslade;
import com.babas.views.tabs.TabBranchs;
import com.babas.views.tabs.TabNewTraslade;
import com.babas.views.tabs.TabRecordTransfers;
import com.babas.views.tabs.TabUsers;
import com.formdev.flatlaf.extras.components.FlatToggleButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuTraslade {
    private FlatToggleButton btnNewTraslade;
    private FlatToggleButton btnRecordTraslades;
    private JPanel contentPane;
    private TabbedPane tabbedPane;
    private TabRecordTransfers tabRecordTransfers;

    public MenuTraslade(TabbedPane tabbedPane){
        this.tabbedPane=tabbedPane;
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
    public void loadNewTraslade(){
        DTraslade dTraslade=new DTraslade(new Transfer(),false);
        dTraslade.setVisible(true);
    }

    public void loadRecordTraslades(){
        Utilities.despintarButton(btnNewTraslade);
        Utilities.buttonSelected(btnRecordTraslades);
        if (tabRecordTransfers == null) {
            tabRecordTransfers = new TabRecordTransfers();
        }
        if (tabbedPane.indexOfTab(tabRecordTransfers.getTabPane().getTitle())==-1) {
            tabRecordTransfers = new TabRecordTransfers();
            tabRecordTransfers.getTabPane().setOption(btnRecordTraslades);
            tabbedPane.addTab(tabRecordTransfers.getTabPane().getTitle(), tabRecordTransfers.getTabPane().getIcon(), tabRecordTransfers.getTabPane());
        }
        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabRecordTransfers.getTabPane().getTitle()));
    }
    public JPanel getContentPane() {
        contentPane.updateUI();
        btnRecordTraslades.updateUI();
        btnNewTraslade.updateUI();
        return contentPane;
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        contentPane =new CustomPane();
    }
}
