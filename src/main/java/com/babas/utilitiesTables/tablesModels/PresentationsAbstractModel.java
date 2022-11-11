package com.babas.utilitiesTables.tablesModels;

import com.babas.models.Presentation;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;

public class PresentationsAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"NOMBRE","CANTIDAD","PRECIO","DEFECTO","",""};
    private final Class[] typeColumns={String.class,Integer.class, Double.class, String.class, JButton.class, JButton.class};
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
        return typeColumns[columnIndex].equals(JButton.class);
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Presentation presentation= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return presentation.getName();
            case 1:
                return presentation.getQuantity();
            case 2:
                return Utilities.moneda.format(presentation.getPriceDefault().getPrice());
            case 3:
                return presentation.isDefault()?"SI":"NO";
            case 4:
                return new JButtonAction("x16/editar.png");
            default:
                return new JButtonAction("x16/remove.png");
        }
    }

    public List<Presentation> getList(){
        return list;
    }
}
