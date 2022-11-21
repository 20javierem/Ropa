package com.babas.utilitiesTables.tablesModels;

import com.babas.models.Quotation;
import com.babas.models.Sale;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;

public class QuotationAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"NRO.","FECHA","VÁLIDO","SUCURSAL","CLIENTE","SUBTOTAL","DESCUENTO","TOTAL","",""};
    private final Class[] typeColumns={Long.class,Date.class,Date.class,String.class,String.class, Double.class,Double.class,Double.class,JButton.class,JButton.class};
    private final List<Quotation> list;

    public QuotationAbstractModel(List<Quotation> list){
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
        Quotation quotation= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return quotation.getNumberQuotation();
            case 1:
                return quotation.getCreated();
            case 2:
                return quotation.getEnded();
            case 3:
                return quotation.getBranch().getName();
            case 4:
                return quotation.getClient()!=null?quotation.getClient().getNames():"-- Sin cliente --";
            case 5:
                return Utilities.moneda.format(quotation.getTotal());
            case 6:
                return Utilities.moneda.format(quotation.getDiscount());
            case 7:
                return Utilities.moneda.format(quotation.getTotalCurrent());
            case 8:
                return new JButtonAction("x16/checkbox.png");
            default:
                return new JButtonAction("x16/mostrarContraseña.png");
        }
    }

    public List<Quotation> getList(){
        return list;
    }
}
