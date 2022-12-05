package com.babas.utilitiesTables.tablesModels;

import com.babas.App;
import com.babas.models.Client;
import com.babas.models.User;
import com.babas.utilitiesTables.buttonEditors.JButtonAction;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;

public class ClientAbstractModel extends AbstractTableModel {
    private final String[] nameColumns={"DNI/RUC","CLIENTE","CELULAR","DIRECCIÓN",""};
    private final Class[] typeColumns={String.class,String.class, String.class,String.class,JButton.class};
    private final List<Client> list;

    public ClientAbstractModel(List<Client> list){
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
        Client client= list.get(rowIndex);
        switch (columnIndex){
            case 0:
                return client.getDni();
            case 1:
                return client.getNames();
            case 2:
                return client.getPhone();
            case 3:
                return client.getMail();
            default:
                return new JButtonAction(new FlatSVGIcon(App.class.getResource("icons/svg/edit.svg")));
        }
    }

    public List<Client> getList(){
        return list;
    }
}
