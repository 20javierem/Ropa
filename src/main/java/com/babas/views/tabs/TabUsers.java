package com.babas.views.tabs;

import com.babas.custom.TabPane;
import com.babas.models.Branch;
import com.babas.models.User;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorProduct;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorUser;
import com.babas.utilitiesTables.tablesCellRendered.BranchCellRendered;
import com.babas.utilitiesTables.tablesCellRendered.UserCellRendered;
import com.babas.utilitiesTables.tablesModels.BranchAbstractModel;
import com.babas.utilitiesTables.tablesModels.UserAbstractModel;
import com.babas.views.dialogs.DBranch;
import com.babas.views.dialogs.DUser;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.formdev.flatlaf.icons.FlatSearchIcon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TabUsers {
    private TabPane tabPane;
    private JButton btnNewUser;
    private FlatTable table;
    private FlatTextField flatTextField;
    private JButton btnClearFilters;
    private UserAbstractModel model;

    public TabUsers(){
        init();
        btnNewUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewUser();
            }
        });
    }

    private void loadNewUser(){
        DUser dUser=new DUser(new User());
        dUser.setVisible(true);
    }

    private void init(){
        tabPane.setTitle("Usuarios");
        tabPane.getActions().addActionListener(e -> model.fireTableDataChanged());
        loadTable();
        loadIcons();
    }
    private void loadIcons(){
        flatTextField.setLeadingIcon(new FlatSearchIcon());
    }
    private void loadTable(){
        model=new UserAbstractModel(FPrincipal.users);
        table.setModel(model);
        UserCellRendered.setCellRenderer(table);
        UtilitiesTables.headerNegrita(table);
        table.getColumnModel().getColumn(model.getColumnCount() -1).setCellEditor(new JButtonEditorUser(false));
        table.getColumnModel().getColumn(model.getColumnCount() - 2).setCellEditor(new JButtonEditorUser(true));
    }

    public TabPane getTabPane() {
        return tabPane;
    }
}
