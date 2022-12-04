package com.babas.utilitiesTables.tablesModels;

import com.babas.App;
import com.babas.models.Transfer;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TransferAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"NRO.","CREADO","ACTUALIZADO","DESCRIPCIÓN","ORIGEN","DESTINO","PRODUCTOS","ESTADO","",""};
    private final Class[] typeColumns={Long.class,Date.class,Date.class,String.class,String.class, String.class, Integer.class,String.class,JButton.class,JButton.class};
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
                return transfer.getNumberTransfer();
            case 1:
                return transfer.getCreated();
            case 2:
                return transfer.getUpdated()==null?transfer.getCreated():transfer.getUpdated();
            case 3:
                return transfer.getDescription();
            case 4:
                return Objects.equals(transfer.getSource().getId(), transfer.getDestiny().getId()) ?"INGRESO":transfer.getSource().getName();
            case 5:
                return transfer.getDestiny().getName();
            case 6:
                return transfer.getProductsTransfers();
            case 7:
                return transfer.getState()==0?"EN ESPERA":transfer.getState()==1?"COMPLETADO":"CANCELADO";
            case 8:
                return new JButtonAction("x16/mostrarContraseña.png");
            default:
                return new JButtonAction(new FlatSVGIcon(App.class.getResource("icons/svg/error.svg")));
        }
    }

    public List<Transfer> getList(){
        return list;
    }
}
