package com.babas.views.dialogs;

import com.babas.models.Category;
import com.babas.models.Color;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorCategory;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorColor;
import com.babas.utilitiesTables.tablesCellRendered.ColorCellRendered;
import com.babas.utilitiesTables.tablesModels.CategoryAbstractModel;
import com.babas.utilitiesTables.tablesModels.ColorAbstractModel;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatTable;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DAllCategorys extends JDialog {
    private JPanel contentPane;
    private FlatTable table;
    private FlatButton btnNew;
    private FlatButton btnHecho;
    private CategoryAbstractModel model;
    private ActionListener actionListener;

    public DAllCategorys() {
        super(Utilities.getJFrame(), "Todas las categorías", true);
        init();
        btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNew();
            }
        });
        btnHecho.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHecho();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onHecho();
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onHecho();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void init() {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnHecho);
        actionListener = e -> model.fireTableDataChanged();
        Utilities.getActionsOfDialog().addActionListener(actionListener);
        loadTable();
        pack();
        setResizable(false);
        setLocationRelativeTo(getOwner());
    }

    private void loadTable() {
        model = new CategoryAbstractModel(FPrincipal.categories);
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        ColorCellRendered.setCellRenderer(table);
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorCategory(false));
        table.getColumnModel().getColumn(model.getColumnCount() - 2).setCellEditor(new JButtonEditorCategory(true));
    }

    private void loadNew() {
        DCategory dCategory = new DCategory(new Category());
        dCategory.setVisible(true);
    }

    private void onHecho() {
        Utilities.getActionsOfDialog().removeActionListener(actionListener);
        dispose();
    }

}
