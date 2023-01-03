package com.babas.utilitiesTables.tablesModels;

import com.babas.App;
import com.babas.models.DetailRental;
import com.babas.models.DetailReserve;
import com.babas.models.DetailSale;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class DetailReserveAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"CÓDIGO","PRODUCTO","COLOR","TALLA","CANTIDAD","PRESENTACIÓN","PRECIO","SUBTOTAL",""};
    private final Class[] typeColumns={Long.class,String.class,String.class,String.class,Integer.class,String.class,DetailSale.class,DetailSale.class, JButton.class};
    private final List<DetailReserve> list;

    public DetailReserveAbstractModel(List<DetailReserve> list){
        this.list=list;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        DetailReserve detailRental=list.get(rowIndex);
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
        DetailReserve detailReserve= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return detailReserve.getProduct().getBarcode();
            case 1:
                return detailReserve.getProduct().getStyle().getName();
            case 2:
                return detailReserve.getProduct().getColor().getName();
            case 3:
                return detailReserve.getProduct().getSize().getName();
            case 4:
                return detailReserve.getQuantity();
            case 5:
            case 6:
                return detailReserve;
            case 7:
                return Utilities.moneda.format(detailReserve.getSubtotal());
            default:
                return new JButtonAction("error");
        }
    }

    public List<DetailReserve> getList(){
        return list;
    }
}