package com.babas.views.menus;

import com.babas.custom.CustomPane;
import com.babas.custom.TabbedPane;
import com.babas.utilities.Utilities;
import com.formdev.flatlaf.FlatLaf;

import javax.swing.*;

public class MenuManage {
    private JToggleButton btnUsers;
    private JToggleButton btnBranchs;
    private JPanel contentPane;
    private TabbedPane tabbedPane;

    public MenuManage(TabbedPane tabbedPane){
        this.tabbedPane=tabbedPane;
    }
    public JPanel getContentPane() {
        contentPane.updateUI();
        btnBranchs.updateUI();
        btnUsers.updateUI();
        return contentPane;
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        contentPane =new CustomPane();
    }
}
