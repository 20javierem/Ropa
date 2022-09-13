package com.babas.utilitiesTables.tablesModels;

import com.babas.models.Brand;
import com.babas.models.Color;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class BrandAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"NOMBRE","ESTILOS","",""};
    private final Class[] typeColumns={String.class,Integer.class,JButton.class,JButton.class};
    private final List<Brand> list;

    public BrandAbstractModel(List<Brand> list){
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
        Brand brand= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return brand.getName();
            case 1:
                return brand.getStyles().size();
            case 2:
                return new JButtonAction("x16/editar.png");
            default:
                return new JButtonAction("x16/remove.png");
        }
    }

    public List<Brand> getList(){
        return list;
    }
}
