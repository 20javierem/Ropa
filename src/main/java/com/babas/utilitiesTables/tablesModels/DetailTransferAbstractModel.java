package com.babas.utilitiesTables.tablesModels;

import com.babas.App;
import com.babas.models.Color;
import com.babas.models.DetailTransfer;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class DetailTransferAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"CÓDIGO","PRODUCTO","COLOR","TALLA","CANTIDAD",""};
    private final Class[] typeColumns={Long.class,String.class,String.class,String.class,Integer.class, JButton.class};
    private final List<DetailTransfer> list;

    public DetailTransferAbstractModel(List<DetailTransfer> list){
        this.list=list;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        DetailTransfer detailTransfer=list.get(rowIndex);
        if(columnIndex==4){
            detailTransfer.setQuantity((Integer) aValue);
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
        return columnIndex == 4 || typeColumns[columnIndex].equals(JButton.class);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DetailTransfer detailTransfer= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return detailTransfer.getProduct().getBarcode();
            case 1:
                return detailTransfer.getProduct().getStyle().getName();
            case 2:
                return detailTransfer.getProduct().getColor().getName();
            case 3:
                return detailTransfer.getProduct().getSize().getName();
            case 4:
                return detailTransfer.getQuantity();
            default:
                return new JButtonAction(new FlatSVGIcon(App.class.getResource("icons/svg/error.svg")));
        }
    }

    public List<DetailTransfer> getList(){
        return list;
    }
}