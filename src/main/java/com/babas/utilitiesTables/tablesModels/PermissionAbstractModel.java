package com.babas.utilitiesTables.tablesModels;

import com.babas.App;
import com.babas.models.Permission;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PermissionAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"NOMBRE","USUARIOS",""};
    private final Class[] typeColumns={String.class,Integer.class,JButton.class,JButton.class};
    private final List<Permission> list;

    public PermissionAbstractModel(List<Permission> list){
        this.list=list;
    }

    @Override
    public String getColumnName(int col) {
        return nameColumns[col];
    }
    @Override
    public Class getColumnClass(int col) {
        return typeColumns[col];
    }
    @Override
    public int getRowCount() {
        return list.size();
    }
    @Override
    public int getColumnCount() {
        return nameColumns.length;
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return typeColumns[columnIndex].equals(JButton.class);
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Permission permission = list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return permission.getNameGroup();
            case 1:
                return permission.getUsers().size();
            default:
                return new JButtonAction("edit");
        }
    }

    public List<Permission> getList(){
        return list;
    }
}
