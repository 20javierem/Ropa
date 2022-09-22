package com.babas.utilitiesTables.tablesModels;

import com.babas.models.Transfer;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TransferAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"CREADO","ACTUALIZADO","DESCRIPCIÓN","ORIGEN","DESTINO","PRODUCTOS","ESTADO","",""};
    private final Class[] typeColumns={Date.class,Date.class,String.class,String.class, String.class, Integer.class,String.class,JButton.class,JButton.class};
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
                return transfer.getCreated();
            case 1:
                return transfer.getUpdated()==null?transfer.getCreated():transfer.getUpdated();
            case 2:
                return transfer.getDescription();
            case 3:
                return Objects.equals(transfer.getSource().getId(), transfer.getDestiny().getId()) ?"INGRESO":transfer.getSource().getName();
            case 4:
                return transfer.getDestiny().getName();
            case 5:
                return transfer.getProductsTransfers();
            case 6:
                return transfer.getState()==0?"EN ESPERA":transfer.getState()==1?"COMPLETADO":"CANCELADO";
            case 7:
                return new JButtonAction("x16/mostrarContraseña.png");
            default:
                return new JButtonAction("x16/remove.png");
        }
    }

    public List<Transfer> getList(){
        return list;
    }
}
