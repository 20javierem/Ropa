package com.babas.utilitiesTables.tablesModels;

import com.babas.App;
import com.babas.models.Category;
import com.babas.models.Color;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CategoryAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"NOMBRE","ESTILOS","",""};
    private final Class[] typeColumns={String.class,Integer.class,JButton.class,JButton.class};
    private final List<Category> list;

    public CategoryAbstractModel(List<Category> list){
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
        Category category= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return category.getName();
            case 1:
                return category.getStyles().size();
            case 2:
                return new JButtonAction(new FlatSVGIcon(App.class.getResource("icons/svg/edit.svg")));
            default:
                return new JButtonAction(new FlatSVGIcon(App.class.getResource("icons/svg/error.svg")));
        }
    }

    public List<Category> getList(){
        return list;
    }
}
