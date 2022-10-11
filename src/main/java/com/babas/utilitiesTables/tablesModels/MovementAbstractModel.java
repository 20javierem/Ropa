package com.babas.utilitiesTables.tablesModels;
import com.babas.models.Movement;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.UtilitiesTables;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;

public class MovementAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"NRO.","FECHA","DESCRIPCIÓN","TIPO","MONTO"};
    private final Class[] typeColumns={Long.class, Date.class,String.class,String.class,Double.class};
    private final List<Movement> list;

    public MovementAbstractModel(List<Movement> list){
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
        Movement movement= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return movement.getId();
            case 1:
                return movement.getCreated();
            case 2:
                return movement.getDescription();
            case 3:
                return movement.isEntrance()?"INGRESO":"RETIRO";
            default:
                return Utilities.moneda.format(movement.getAmount());
        }
    }

    public List<Movement> getList(){
        return list;
    }
}
