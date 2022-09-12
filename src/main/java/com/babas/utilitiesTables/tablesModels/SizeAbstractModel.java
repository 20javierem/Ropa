package com.babas.utilitiesTables.tablesModels;

import com.babas.models.Category;
import com.babas.models.Size;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SizeAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"NOMBRE","",""};
    private final Class[] typeColumns={String.class,JButton.class,JButton.class};
    private final List<Size> list;

    public SizeAbstractModel(List<Size> list){
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
        if (typeColumns[columnIndex].equals(JButton.class)){
            return true;
        }
        return false;
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Size size= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return size.getName();
            case 1:
                return new JButtonAction("x16/editar.png");
            default:
                return new JButtonAction("x16/remove.png");
        }
    }

    public List<Size> getList(){
        return list;
    }
}
