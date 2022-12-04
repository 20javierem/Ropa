package com.babas.utilitiesTables.tablesModels;

import com.babas.App;
import com.babas.models.DetailRental;
import com.babas.models.DetailSale;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class DetailRentalAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"CÓDIGO","PRODUCTO","COLOR","TALLA","CANTIDAD","PRESENTACIÓN","PRECIO","SUBTOTAL",""};
    private final Class[] typeColumns={Long.class,String.class,String.class,String.class,Integer.class,String.class,DetailSale.class,DetailSale.class, JButton.class};
    private final List<DetailRental> list;

    public DetailRentalAbstractModel(List<DetailRental> list){
        this.list=list;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        DetailRental detailRental=list.get(rowIndex);
        if(columnIndex==4){
            if((Integer)aValue>0){
                detailRental.setQuantity((Integer) aValue);
                Utilities.getTabbedPane().updateTab();
            }
        }
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
        return columnIndex == 4||columnIndex == 5 ||columnIndex == 6 || typeColumns[columnIndex].equals(JButton.class);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DetailRental detailRental= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return detailRental.getProduct().getBarcode();
            case 1:
                return detailRental.getProduct().getStyle().getName();
            case 2:
                return detailRental.getProduct().getColor().getName();
            case 3:
                return detailRental.getProduct().getSize().getName();
            case 4:
                return detailRental.getQuantity();
            case 5:
            case 6:
                return detailRental;
            case 7:
                return Utilities.moneda.format(detailRental.getSubtotal());
            default:
                return new JButtonAction(new FlatSVGIcon(App.class.getResource("icons/svg/error.svg")));
        }
    }

    public List<DetailRental> getList(){
        return list;
    }
}