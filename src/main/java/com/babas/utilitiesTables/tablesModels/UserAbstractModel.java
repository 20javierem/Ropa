package com.babas.utilitiesTables.tablesModels;

import com.babas.models.Branch;
import com.babas.models.User;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;

public class UserAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"NOMBRES Y APELLIDOS","CELULAR","ULTIMA SESIÓN","ESTADO","SUCURSALES","",""};
    private final Class[] typeColumns={String.class,String.class, Date.class,String.class,Integer.class,JButton.class,JButton.class};
    private final List<User> list;

    public UserAbstractModel(List<User> list){
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
        User user= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return user.getLastName()+", "+user.getFirstName();
            case 1:
                return user.getPhone();
            case 2:
                return user.getLastLogin();
            case 3:
                return user.isActive()?"ACTIVO":"INACTIVO";
            case 4:
                return user.getBranchs().size();
            case 5:
                return new JButtonAction("x16/editar.png");
            default:
                return new JButtonAction("x16/remove.png");
        }
    }

    public List<User> getList(){
        return list;
    }
}
