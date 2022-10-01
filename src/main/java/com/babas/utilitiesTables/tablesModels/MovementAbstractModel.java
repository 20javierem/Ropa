package com.babas.utilitiesTables.tablesModels;
import com.babas.models.Movement;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;

public class MovementAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"NRO.","FECHA","DESCRIPCIÓN","TIPO","MONTO","",""};
    private final Class[] typeColumns={Long.class, Date.class,String.class,String.class,Double.class,JButton.class,JButton.class};
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
                return movement.isEntrance()?"INGRESO":"EGRESO";
            case 4:
                return movement.getAmount();
            case 5:
                return new JButtonAction("x16/editar.png");
            default:
                return new JButtonAction("x16/remove.png");
        }
    }

    public List<Movement> getList(){
        return list;
    }
}
