package com.babas.utilitiesTables.tablesModels;

import com.babas.App;
import com.babas.models.Sale;
import com.babas.models.Transfer;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SaleAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"NRO.","FECHA","ACTUALIZADO","SUCURSAL","CLIENTE","TIPO/PAGO","SUBTOTAL","DESCUENTO","TOTAL","ESTADO","SUNAT","",""};
    private final Class[] typeColumns={Long.class,Date.class,Date.class,String.class,String.class,String.class, Double.class,Double.class,Double.class,String.class,String.class,JButton.class,JButton.class};
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
                return sale.getId();
            case 1:
                return sale.getCreated();
            case 2:
                return sale.getUpdated();
            case 3:
                return sale.getBranch().getName();
            case 4:
                return sale.getStringClient();
            case 5:
                return sale.getStringType();
            case 6:
                return Utilities.moneda.format(sale.getTotal());
            case 7:
                return Utilities.moneda.format(sale.getDiscount());
            case 8:
                return Utilities.moneda.format(sale.getTotalCurrent());
            case 9:
                return sale.getStringStade();
            case 10:
                return sale.getStringSunat();
            case 11:
                return new JButtonAction("x16/mostrarContraseña.png");
            default:
                return new JButtonAction(new FlatSVGIcon(App.class.getResource("icons/svg/error.svg")));
        }
    }

    public List<Sale> getList(){
        return list;
    }
}
