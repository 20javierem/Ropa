package com.babas.utilitiesTables.tablesModels;

import com.babas.models.Presentation;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;

public class PresentationsAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"CANTIDAD","PRECIO","DEFECTO","",""};
    private final Class[] typeColumns={Integer.class, Double.class, String.class, JButton.class, JButton.class};
    private final List<Presentation> list;

    public PresentationsAbstractModel(List<Presentation> list){
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
        Presentation presentation= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return presentation.getQuantity();
            case 1:
                return presentation.getPriceDefault().getPrice();
            case 2:
                return presentation.isDefault()?"SI":"NO";
            case 3:
                return new JButtonAction("x16/editar.png");
            default:
                return new JButtonAction("x16/remove.png");
        }
    }

    public List<Presentation> getList(){
        return list;
    }
}
