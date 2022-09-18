package com.babas.utilitiesTables.tablesModels;

import com.babas.models.Product;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ProductAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"CÓDIGO","NOMBRE","PRECIO","COLOR","GÉNERO","TALLA","TOTAL-STOCK","","",""};
    private final Class[] typeColumns={Long.class,String.class,Double.class,String.class,String.class,String.class,Integer.class,JButton.class,JButton.class,JButton.class};
    private final List<Product> list;

    public ProductAbstractModel(List<Product> list){
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
            case 1:
                return product.getStyle().getName();
            case 2:
                return product.getStyle().getPresentationDefault().getPriceDefault().getPrice();
            case 3:
                return product.getColor().getName();
            case 4:
                return product.getSex().getName();
            case 5:
                return product.getSize().getName();
            case 6:
                return product.getStockTotal();
            case 7:
                return new JButtonAction("x16/mostrarContraseña.png");
            case 8:
                return new JButtonAction("x16/editar.png");
            default:
                return new JButtonAction("x16/remove.png");
        }
    }

    public List<Product> getList(){
        return list;
    }
}
