package com.babas.utilitiesTables.tablesModels;

import com.babas.models.Color;
import com.babas.models.DetailTransfer;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class DetailTransferAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"CÓDIGO","PRODUCTO","COLOR","TALLA","CANTIDAD","",""};
    private final Class[] typeColumns={Long.class,String.class,String.class,String.class,Integer.class, JButton.class,JButton.class};
    private final List<DetailTransfer> list;

    public DetailTransferAbstractModel(List<DetailTransfer> list){
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
            case 5:
                return new JButtonAction("x16/editar.png");
            default:
                return new JButtonAction("x16/remove.png");
        }
    }

    public List<DetailTransfer> getList(){
        return list;
    }
}