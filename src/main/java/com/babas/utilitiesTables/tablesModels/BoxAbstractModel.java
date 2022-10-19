package com.babas.utilitiesTables.tablesModels;

import com.babas.models.Box;
import com.babas.models.Sex;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;

public class BoxAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"SUCURSAL","NOMBRE","ACTIVA","",""};
    private final Class[] typeColumns={String.class,String.class, String.class,JButton.class,JButton.class};
    private final List<Box> list;

    public BoxAbstractModel(List<Box> list){
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
        Box box= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return box.getBranch().getName();
            case 1:
                return box.getName();
            case 2:
                return box.isActive()?"ACTIVA":"DESACTIVADA";
            case 3:
                return new JButtonAction("x16/editar.png");
            default:
                return new JButtonAction("x16/remove.png");
        }
    }

    public List<Box> getList(){
        return list;
    }
}
