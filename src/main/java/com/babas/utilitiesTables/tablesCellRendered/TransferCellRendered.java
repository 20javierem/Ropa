package com.babas.utilitiesTables.tablesCellRendered;

import com.babas.views.dialogs.DesingTxtTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import static com.babas.utilitiesTables.UtilitiesTables.buscarTexto2;

public class TransferCellRendered extends DefaultTableCellRenderer {

    public TransferCellRendered(){

    }

    public static void setCellRenderer(JTable table){
        TransferCellRendered cellRendered=new TransferCellRendered();
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
            button.setForeground(component.getForeground());
            return button;
        }else{
            DesingTxtTable componente=buscarTexto2(null,value,column,component);
            switch(table.getColumnName(column)){
                case "NRO.":
                    componente.setHorizontalAlignment(SwingConstants.CENTER);
                    table.getColumn(table.getColumnName(column)).setMaxWidth(90);
                    table.getColumn(table.getColumnName(column)).setMinWidth(90);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(90);
                    break;
                case "ORIGEN":
                case "DESTINO":
                    componente.setHorizontalAlignment(SwingConstants.LEFT);
//                    table.getColumn(table.getColumnName(column)).setMaxWidth(210);
                    table.getColumn(table.getColumnName(column)).setMinWidth(210);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(210);
                    break;
                case "PRODUCTOS":
                    componente.setHorizontalAlignment(SwingConstants.CENTER);
                    table.getColumn(table.getColumnName(column)).setMaxWidth(100);
                    table.getColumn(table.getColumnName(column)).setMinWidth(100);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(100);
                    break;
                case "CREADO":
                case "ACTUALIZADO":
                case "ESTADO":
                    componente.setHorizontalAlignment(SwingConstants.CENTER);
                    table.getColumn(table.getColumnName(column)).setMaxWidth(150);
                    table.getColumn(table.getColumnName(column)).setMinWidth(150);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(150);
                    break;
                default:
                    componente.setHorizontalAlignment(SwingConstants.LEFT);
                    break;
            }
            return componente.getContentPane();
        }
    }

}
