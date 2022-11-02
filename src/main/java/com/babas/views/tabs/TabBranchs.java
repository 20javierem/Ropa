package com.babas.views.tabs;

import com.babas.custom.TabPane;
import com.babas.models.Branch;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorBranch;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorUser;
import com.babas.utilitiesTables.tablesCellRendered.BranchCellRendered;
import com.babas.utilitiesTables.tablesModels.BranchAbstractModel;
import com.babas.views.dialogs.DBranch;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.formdev.flatlaf.icons.FlatSearchIcon;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.apache.commons.collections.iterators.UnmodifiableIterator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TabBranchs {
    private TabPane tabPane;
    private JButton btnNewBranch;
    private FlatTable table;
    private FlatTextField flatTextField;
    private BranchAbstractModel model;

    public TabBranchs() {
        init();
        btnNewBranch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewBranch();
            }
        });
    }

    private void loadNewBranch() {
        DBranch dBranch = new DBranch(new Branch());
        dBranch.setVisible(true);
    }

    private void init() {
        tabPane.setTitle("Sucursales");
        tabPane.getActions().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.fireTableDataChanged();
                Utilities.getLblCentro().setText("Sucursales");
            }
        });
        loadTable();
        loadIcons();
        Utilities.getLblCentro().setText("Sucursales");
    }

    private void loadIcons() {
        flatTextField.setLeadingIcon(new FlatSearchIcon());
    }

    private void loadTable() {
        model = new BranchAbstractModel(FPrincipal.branchs);
        table.setModel(model);
        BranchCellRendered.setCellRenderer(table);
        UtilitiesTables.headerNegrita(table);
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorBranch(false));
        table.getColumnModel().getColumn(model.getColumnCount() - 2).setCellEditor(new JButtonEditorBranch(true));
    }

    public TabPane getTabPane() {
        return tabPane;
    }

}
