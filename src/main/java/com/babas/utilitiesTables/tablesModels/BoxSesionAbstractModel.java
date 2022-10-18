package com.babas.utilitiesTables.tablesModels;

import com.babas.models.BoxSession;
import com.babas.models.Sale;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;

public class BoxSesionAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"CAJA","SUCURSAL","USUARIO","INICIO","CIERRE","EFECTIVO","TRANSFERENCIA","TOTAL",""};
    private final Class[] typeColumns={String.class,String.class,String.class,Date.class,Date.class,String.class,String.class, String.class,JButton.class};
    private final List<BoxSession> list;

    public BoxSesionAbstractModel(List<BoxSession> list){
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
        BoxSession boxSession= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return boxSession.getBox().getName();
            case 1:
                return boxSession.getBox().getBranch().getName();
            case 2:
                return boxSession.getUser().getFirstName();
            case 3:
                return boxSession.getCreated();
            case 4:
                return boxSession.getUpdated();
            case 5:
                return Utilities.moneda.format(boxSession.getTotalCash());
            case 6:
                return Utilities.moneda.format(boxSession.getTotalTransfers());
            case 7:
                return Utilities.moneda.format(boxSession.getAmountTotal());
            default:
                return new JButtonAction("x16/mostrarContraseña.png","Detalle");
        }
    }

    public List<BoxSession> getList(){
        return list;
    }
}
