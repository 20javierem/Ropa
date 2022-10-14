package com.babas.utilitiesTables.tablesModels;

import com.babas.models.Rental;
import com.babas.models.Sale;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;

public class RentalAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"NRO.","FECHA","SUCURSAL","CLIENTE","TIPO/PAGO","ESTADO","SUBTOTAL","GARANTÍA","DESCUENTO","TOTAL","MULTA","TOTAL-ACTUAL","",""};
    private final Class[] typeColumns={Long.class,Date.class,String.class,String.class,String.class,String.class, Double.class,Double.class,Double.class,Double.class,Double.class,Double.class,JButton.class,JButton.class};
    private final List<Rental> list;

    public RentalAbstractModel(List<Rental> list){
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
        Rental rental = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rental.getNumberRental();
            case 1:
                return rental.getCreated();
            case 2:
                return rental.getBranch().getName();
            case 3:
                return rental.getClient().getNames();
            case 4:
                return rental.isCash() ? "EFECTIVO" : "TRANSFERENCIA";
            case 5:
                return rental.isActive() ? "EN ALQUILER" : "FINALIZADA";
            case 6:
                return Utilities.moneda.format(rental.getTotal());
            case 7:
                return Utilities.moneda.format(rental.getWarranty());
            case 8:
                return Utilities.moneda.format(rental.getDiscount());
            case 9:
                return Utilities.moneda.format(rental.getTotalCurrent());
            case 10:
                return Utilities.moneda.format(rental.getPenalty());
            case 11:
                return Utilities.moneda.format(rental.getTotalCurrentWithPenalty());
            case 12:
                return new JButtonAction("x16/mostrarContraseña.png","Finalizar");
            default:
                return new JButtonAction("x16/mostrarContraseña.png","Ticket");
        }
    }


    public List<Rental> getList(){
        return list;
    }
}
