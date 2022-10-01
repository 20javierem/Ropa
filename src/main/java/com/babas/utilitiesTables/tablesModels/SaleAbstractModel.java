package com.babas.utilitiesTables.tablesModels;

import com.babas.models.Sale;
import com.babas.models.Transfer;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SaleAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"NRO.","FECHA","ACTUALIZADO","SUCURSAL","CLIENTE","TIPO/PAGO","ESTADO","SUBTOTAL","DESCUENTO","TOTAL","",""};
    private final Class[] typeColumns={Long.class,Date.class,Date.class,String.class,String.class,String.class,String.class, Double.class,Double.class,Double.class,JButton.class,JButton.class};
    private final List<Sale> list;

    public SaleAbstractModel(List<Sale> list){
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
        Sale sale= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return sale.getNumberSale();
            case 1:
                return sale.getCreated();
            case 2:
                return sale.getUpdated();
            case 3:
                return sale.getBranch().getName();
            case 4:
                return sale.getClient()!=null?sale.getClient().getNames():"-- Sin cliente --";
            case 5:
                return sale.isCash()?"EFECTIVO":"TRANSFERENCIA";
            case 6:
                return sale.isActive()?"REALIZADA":"CANCELADA";
            case 7:
                return sale.getTotal();
            case 8:
                return sale.getDiscount();
            case 9:
                return sale.getTotalCurrent();
            case 10:
                return new JButtonAction("x16/mostrarContraseña.png");
            default:
                return new JButtonAction("x16/remove.png");
        }
    }

    public List<Sale> getList(){
        return list;
    }
}
