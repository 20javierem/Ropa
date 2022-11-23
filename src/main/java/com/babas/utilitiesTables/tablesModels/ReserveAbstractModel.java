package com.babas.utilitiesTables.tablesModels;

import com.babas.models.Reserve;
import com.babas.models.Sale;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;

public class ReserveAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"NRO.","FECHA","SUCURSAL","CLIENTE","TIPO/PAGO","ESTADO","TOTAL","ADELANTO","POR PAGAR","","",""};
    private final Class[] typeColumns={Long.class,Date.class,String.class,String.class,String.class,String.class, String.class,String.class,String.class,JButton.class,JButton.class,JButton.class};
    private final List<Reserve> list;

    public ReserveAbstractModel(List<Reserve> list){
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
        Reserve reserve= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return reserve.getNumberReserve();
            case 1:
                return reserve.getCreated();
            case 2:
                return reserve.getBranch().getName();
            case 3:
                return reserve.getClient().getNames();
            case 4:
                return reserve.getStringType();
            case 5:
                return reserve.getStringStade();
            case 6:
                return Utilities.moneda.format(reserve.getTotal());
            case 7:
                return Utilities.moneda.format(reserve.getAdvance());
            case 8:
                return Utilities.moneda.format(reserve.getToCancel());
            case 9:
                return new JButtonAction("x16/checkbox.png");
            case 10:
                return new JButtonAction("x16/mostrarContraseña.png");
            default:
                return new JButtonAction("x16/remove.png");
        }
    }

    public List<Reserve> getList(){
        return list;
    }
}
