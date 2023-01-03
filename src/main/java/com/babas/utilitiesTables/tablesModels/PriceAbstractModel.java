package com.babas.utilitiesTables.tablesModels;

import com.babas.App;
import com.babas.models.Price;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Objects;

public class PriceAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"PRECIO","DEFECTO","",""};
    private final Class[] typeColumns={Double.class, String.class, JButton.class, JButton.class};
    private final List<Price> list;

    public PriceAbstractModel(List<Price> list){
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
        Price price= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return Utilities.moneda.format(price.getPrice());
            case 1:
                return price.isDefault()?"SI":"NO";
            case 2:
                return new JButtonAction("edit");
            default:
                return new JButtonAction("error");
        }
    }

    public List<Price> getList(){
        return list;
    }
}
