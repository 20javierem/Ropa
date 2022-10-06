package com.babas.utilitiesTables.tablesModels;
import com.babas.models.Stock;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class StockProductAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"SUCURSAL","PRODUCTO","STOCK-ACTUAL","EN ALQUILER"};
    private final Class[] typeColumns={String.class,String.class,Integer.class,Integer.class};
    private final List<Stock> list;

    public StockProductAbstractModel(List<Stock> list){
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
        Stock stock= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return stock.getBranch().getName();
            case 1:
                return stock.getProduct().getStyle().getName()+" / "+stock.getProduct().getSex().getName()+" / "+stock.getProduct().getBrand().getName()+" / "+stock.getProduct().getSize().getName()+" / "+stock.getProduct().getColor().getName();
            case 2:
                return stock.getQuantity();
            default:
                return stock.getOnRental();
        }
    }

    public List<Stock> getList(){
        return list;
    }
}