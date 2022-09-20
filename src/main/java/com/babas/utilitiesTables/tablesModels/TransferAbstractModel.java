package com.babas.utilitiesTables.tablesModels;

import com.babas.models.Transfer;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TransferAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"DESCRIPCIÓN","ORIGEN","DESTINO","PRODUCTOS","ESTADO",""};
    private final Class[] typeColumns={String.class,String.class, String.class, Integer.class,String.class,JButton.class};
    private final List<Transfer> list;

    public TransferAbstractModel(List<Transfer> list){
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
        Transfer transfer= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return transfer.getDescription();
            case 1:
                return transfer.getSource().getName();
            case 2:
                return transfer.getDestiny().getName();
            case 3:
                return transfer.getProductsTransfers();
            case 4:
                return transfer.getState()==0?"EN ESPERA":transfer.getState()==1?"ACEPTADO":"RECHAZADO";
            default:
                return new JButtonAction("x16/mostrarContraseña.png");
        }
    }

    public List<Transfer> getList(){
        return list;
    }
}
