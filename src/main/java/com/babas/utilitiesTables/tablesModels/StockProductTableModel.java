package com.babas.utilitiesTables.tablesModels;
import com.babas.models.Stock;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class StockProductTableModel extends AbstractTableModel {
    private final String[] nameColumns={"CÓDIGO","NOMBRE","PRECIO","COLOR","GÉNERO","TALLA","SUCURSAL","STOCK",""};
    private final Class[] typeColumns={Long.class,String.class,Double.class,String.class,String.class,String.class,String.class,Integer.class,JButton.class};
    private final List<Stock> list;

    public StockProductTableModel(List<Stock> list){
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
                return stock.getProduct().getBarcode();
            case 1:
                return stock.getProduct().getStyle().getName();
            case 2:
                return stock.getProduct().getStyle().getPresentationDefault().getPriceDefault().getPrice();
            case 3:
                return stock.getProduct().getColor().getName();
            case 4:
                return stock.getProduct().getSex().getName();
            case 5:
                return stock.getProduct().getSize().getName();
            case 6:
                return stock.getBranch().getName();
            case 7:
                return stock.getQuantity();
            default:
                return new JButtonAction("x16/mostrarContraseña.png");
        }
    }

    public List<Stock> getList(){
        return list;
    }
}