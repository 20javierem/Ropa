package com.babas.views.dialogs;

import com.babas.models.Permission;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorCategory;
import com.babas.utilitiesTables.buttonEditors.JButtonEditorPermission;
import com.babas.utilitiesTables.tablesCellRendered.GroupCellRendered;
import com.babas.utilitiesTables.tablesModels.PermissionAbstractModel;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatTable;

import javax.swing.*;
import java.awt.event.*;

public class DallGroups extends JDialog{
    private JPanel contentPane;
    private FlatTable table;
    private FlatButton btnNew;
    private FlatButton btnHecho;
    private ActionListener actionListener;
    private PermissionAbstractModel model;

    public DallGroups(){
        super(Utilities.getJFrame(),"Todos los grupos",true);
        init();
        btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewGroup();
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

    private void loadNewGroup(){
        DGroupPermition dGroupPermition=new DGroupPermition(new Permission());
        dGroupPermition.setVisible(true);
    }

    private void init(){
        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnHecho);
        actionListener= e -> model.fireTableDataChanged();
        Utilities.getActionsOfDialog().addActionListener(actionListener);
        loadTable();
        pack();
        setLocationRelativeTo(getOwner());
    }
    private void loadTable(){
        model=new PermissionAbstractModel(FPrincipal.groupPermnitions);
        table.setModel(model);
        UtilitiesTables.headerNegrita(table);
        GroupCellRendered.setCellRenderer(table);
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellEditor(new JButtonEditorPermission());
    }
    private void onHecho(){
        Utilities.getActionsOfDialog().removeActionListener(actionListener);
        dispose();
    }

}
