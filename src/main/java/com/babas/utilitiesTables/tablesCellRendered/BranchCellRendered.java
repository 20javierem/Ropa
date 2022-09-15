package com.babas.utilitiesTables.tablesCellRendered;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import static com.babas.utilitiesTables.UtilitiesTables.buscarTexto;

public class BranchCellRendered extends DefaultTableCellRenderer {

    public BranchCellRendered(){

    }

    public static void setCellRenderer(JTable table){
        BranchCellRendered cellRendered=new BranchCellRendered();
        for (int i=0;i<table.getColumnCount();i++){
            table.getColumnModel().getColumn(i).setCellRenderer(cellRendered);
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component=super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(table.getColumnClass(column).equals(JButton.class)){
            table.getColumnModel().getColumn(column).setMaxWidth(25);
            table.getColumnModel().getColumn(column).setMinWidth(25);
            table.getColumnModel().getColumn(column).setPreferredWidth(25);
            JButton button=(JButton) value;
            button.setBackground(component.getBackground());
            return button;
        }else{
            JTextField componente=buscarTexto(null,value,column,component);
            switch(table.getColumnName(column)){
                case "EMAIL":
                    componente.setHorizontalAlignment(SwingConstants.LEFT);
                    table.getColumn(table.getColumnName(column)).setMaxWidth(210);
                    table.getColumn(table.getColumnName(column)).setMinWidth(210);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(210);
                    break;
                case "ESTADO":
                case "USUARIOS":
                    componente.setHorizontalAlignment(SwingConstants.CENTER);
                    table.getColumn(table.getColumnName(column)).setMaxWidth(120);
                    table.getColumn(table.getColumnName(column)).setMinWidth(120);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(120);
                    break;
                default:
                    componente.setHorizontalAlignment(SwingConstants.LEFT);
                    break;
            }
            return componente;
        }
    }

}
