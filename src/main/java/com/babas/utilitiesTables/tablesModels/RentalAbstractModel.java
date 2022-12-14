package com.babas.utilitiesTables.tablesModels;

import com.babas.App;
import com.babas.models.Rental;
import com.babas.models.Sale;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;

public class RentalAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"NRO.","FECHA","SUCURSAL","CLIENTE","TIPO/PAGO","SUBTOTAL","GARANTÍA","DESCUENTO","RESUMEN","MULTA","TOTAL","TIPO","ESTADO","SUNAT","","","",""};
    private final Class[] typeColumns={String.class,Date.class,String.class,String.class,String.class, Double.class,Double.class,Double.class,Double.class,Double.class,Double.class,String.class, String.class,String.class,JButton.class,JButton.class,JButton.class,JButton.class};
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
                return rental.getStringType();
            case 5:
                return Utilities.moneda.format(rental.getTotal());
            case 6:
                return Utilities.moneda.format(rental.getWarranty());
            case 7:
                return Utilities.moneda.format(rental.getDiscount());
            case 8:
                return Utilities.moneda.format(rental.getTotalCurrent());
            case 9:
                return Utilities.moneda.format(rental.getPenalty());
            case 10:
                return Utilities.moneda.format(rental.getTotalWithDiscount());
            case 11:
                return rental.getTypeStringVoucher();
            case 12:
                return rental.getStringStade();
            case 13:
                return rental.getStringSunat();
            case 14:
                return new JButtonAction("change");
            case 15:
                return new JButtonAction("check");
            case 16:
                return new JButtonAction("show");
            default:
                return new JButtonAction("error");
        }
    }


    public List<Rental> getList(){
        return list;
    }
}
