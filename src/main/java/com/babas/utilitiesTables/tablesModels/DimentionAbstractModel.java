package com.babas.utilitiesTables.tablesModels;

import com.babas.models.Dimention;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class DimentionAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"NOMBRE","PRODUCTOS","",""};
    private final Class[] typeColumns={String.class,Integer.class,JButton.class,JButton.class};
    private final List<Dimention> list;

    public DimentionAbstractModel(List<Dimention> list){
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
        Dimention dimention= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return dimention.getName();
            case 1:
                return dimention.getProducts().size();
            case 2:
                return new JButtonAction("x16/editar.png");
            default:
                return new JButtonAction("x16/remove.png");
        }
    }

    public List<Dimention> getList(){
        return list;
    }
}
