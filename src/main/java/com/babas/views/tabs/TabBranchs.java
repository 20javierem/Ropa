package com.babas.views.tabs;

import com.babas.custom.TabPane;
import com.babas.models.Branch;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.tablesCellRendered.BranchCellRendered;
import com.babas.utilitiesTables.tablesModels.BranchAbstractModel;
import com.babas.views.dialogs.DBranch;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.formdev.flatlaf.icons.FlatSearchIcon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TabBranchs {
    private TabPane tabPane;
    private JButton btnNewBranch;
    private FlatTable table;
    private FlatTextField flatTextField;
    private JButton btnClearFilters;
    private BranchAbstractModel model;

    public TabBranchs(){
        init();
        btnNewBranch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewBranch();
            }
        });
    }

    private void loadNewBranch(){
        DBranch dBranch=new DBranch(new Branch());
        dBranch.setVisible(true);
    }

    private void init(){
        tabPane.setTitle("Sucursales");
        tabPane.getActions().addActionListener(e -> model.fireTableDataChanged());
        loadTable();
        loadIcons();
    }
    private void loadIcons(){
        flatTextField.setLeadingIcon(new FlatSearchIcon());
    }
    private void loadTable(){
        model=new BranchAbstractModel(FPrincipal.branchs);
        table.setModel(model);
        BranchCellRendered.setCellRenderer(table);
        UtilitiesTables.headerNegrita(table);
    }

    public TabPane getTabPane() {
        return tabPane;
    }
}
