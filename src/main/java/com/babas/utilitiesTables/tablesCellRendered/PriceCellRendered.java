package com.babas.utilitiesTables.tablesCellRendered;

import com.babas.views.dialogs.DesingTxtTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import static com.babas.utilitiesTables.UtilitiesTables.buscarTexto2;

public class PriceCellRendered extends DefaultTableCellRenderer {

    public PriceCellRendered(){

    }

    public static void setCellRenderer(JTable table){
        PriceCellRendered cellRendered=new PriceCellRendered();
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
            if ("DEFECTO".equals(table.getColumnName(column))) {
                componente.setHorizontalAlignment(SwingConstants.CENTER);
//                table.getColumn(table.getColumnName(column)).setMaxWidth(80);
//                table.getColumn(table.getColumnName(column)).setMinWidth(80);
                table.getColumn(table.getColumnName(column)).setPreferredWidth(80);
            }else{
                componente.setHorizontalAlignment(SwingConstants.RIGHT);
            }
            return componente.getContentPane();
        }
    }

}
