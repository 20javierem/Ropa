package com.babas.utilitiesTables.tablesCellRendered;

import com.babas.views.dialogs.DesingTxtTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import static com.babas.utilitiesTables.UtilitiesTables.buscarTexto2;

public class PresentationCellRendered extends DefaultTableCellRenderer {

    public PresentationCellRendered(){

    }

    public static void setCellRenderer(JTable table){
        PresentationCellRendered cellRendered=new PresentationCellRendered();
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
                case "CANTIDAD":
                case "DEFECTO":
                case "PRECIO":
                    componente.setHorizontalAlignment(SwingConstants.CENTER);
//                    table.getColumn(table.getColumnName(column)).setMaxWidth(80);
//                    table.getColumn(table.getColumnName(column)).setMinWidth(80);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(80);
                    break;
                case "ACTUALIZADO":
                    componente.setHorizontalAlignment(SwingConstants.CENTER);
//                    table.getColumn(table.getColumnName(column)).setMaxWidth(140);
//                    table.getColumn(table.getColumnName(column)).setMinWidth(140);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(140);
                    break;
                case "NOMBRE":
                    componente.setHorizontalAlignment(SwingConstants.LEFT);
                    break;
                default:
                    componente.setHorizontalAlignment(SwingConstants.RIGHT);
                    break;
            }
            return componente.getContentPane();
        }
    }

}
