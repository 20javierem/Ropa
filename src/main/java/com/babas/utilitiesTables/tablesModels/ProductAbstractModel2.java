package com.babas.utilitiesTables.tablesModels;
import com.babas.models.Product;
import com.babas.models.Stock;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ProductAbstractModel2 extends AbstractTableModel {
    private final String[] nameColumns={"CÓDIGO","PRODUCTO"};
    private final Class[] typeColumns={String.class,String.class};
    private final List<Product> list;

    public ProductAbstractModel2(List<Product> list){
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
        Product product= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return product.getBarcode();
            default:
                return product.getStyle().getName()+" / "+product.getSex().getName()+" / "+product.getBrand().getName()+" / "+product.getSize().getName()+" / "+product.getColor().getName();
        }
    }

    public List<Product> getList(){
        return list;
    }
}