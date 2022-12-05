package com.babas.utilitiesTables.tablesModels;

import com.babas.App;
import com.babas.models.Branch;
import com.babas.models.Product;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class BranchAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"NOMBRE","DIRECCIÓN","EMAIL","ESTADO","USUARIOS","",""};
    private final Class[] typeColumns={String.class,String.class,String.class,String.class,Integer.class,JButton.class,JButton.class};
    private final List<Branch> list;

    public BranchAbstractModel(List<Branch> list){
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
        Branch branch= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return branch.getName();
            case 1:
                return branch.getDirection();
            case 2:
                return branch.getEmail();
            case 3:
                return branch.isActive()?"ACTIVO":"INACTIVO";
            case 4:
                return branch.getUsers().size();
            case 5:
                return new JButtonAction(new FlatSVGIcon(App.class.getResource("icons/svg/edit.svg")));
            default:
                return new JButtonAction(new FlatSVGIcon(App.class.getResource("icons/svg/error.svg")));
        }
    }

    public List<Branch> getList(){
        return list;
    }
}
