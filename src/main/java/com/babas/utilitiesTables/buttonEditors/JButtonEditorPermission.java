package com.babas.utilitiesTables.buttonEditors;

import com.babas.models.Brand;
import com.babas.models.Permission;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.BrandAbstractModel;
import com.babas.utilitiesTables.tablesModels.PermissionAbstractModel;
import com.babas.views.dialogs.DBrand;
import com.babas.views.dialogs.DGroupPermition;
import com.babas.views.frames.FPrincipal;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonEditorPermission extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;

    public JButtonEditorPermission() {
        button=new JButtonAction("x16/editar.png");
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        button.setActionCommand("edit");
        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTable table = (JTable)button.getParent();
        if(table.getSelectedRow()!=-1){
            fireEditingStopped();
            Permission permission=((PermissionAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            DGroupPermition dGroupPermition=new DGroupPermition(permission);
            dGroupPermition.setVisible(true);
            Utilities.updateDialog();
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button;
    }
}
