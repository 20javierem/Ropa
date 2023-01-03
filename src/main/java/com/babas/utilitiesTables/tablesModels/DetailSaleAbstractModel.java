package com.babas.utilitiesTables.tablesModels;

import com.babas.App;
import com.babas.models.DetailSale;
import com.babas.models.DetailTransfer;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class DetailSaleAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"CÓDIGO","PRODUCTO","COLOR","TALLA","CANTIDAD","PRESENTACIÓN","PRECIO","SUBTOTAL",""};
    private final Class[] typeColumns={Long.class,String.class,String.class,String.class,Integer.class,String.class,DetailSale.class,DetailSale.class, JButton.class};
    private final List<DetailSale> list;

    public DetailSaleAbstractModel(List<DetailSale> list){
        this.list=list;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        DetailSale detailSale=list.get(rowIndex);
        if(columnIndex==4){
            if((Integer)aValue>0){
                detailSale.setQuantity((Integer) aValue);
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
        DetailSale detailSale= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return detailSale.getProduct().getBarcode();
            case 1:
                return detailSale.getProduct().getStyle().getName();
            case 2:
                return detailSale.getProduct().getColor().getName();
            case 3:
                return detailSale.getProduct().getSize().getName();
            case 4:
                return detailSale.getQuantity();
            case 5:
            case 6:
                return detailSale;
            case 7:
                return Utilities.moneda.format(detailSale.getSubtotal());
            default:
                return new JButtonAction("error");
        }
    }

    public List<DetailSale> getList(){
        return list;
    }
}