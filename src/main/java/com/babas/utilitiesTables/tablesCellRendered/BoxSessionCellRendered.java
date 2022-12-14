package com.babas.utilitiesTables.tablesCellRendered;

import com.babas.views.dialogs.DesingTxtTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.babas.utilitiesTables.UtilitiesTables.buscarTexto2;

public class BoxSessionCellRendered extends DefaultTableCellRenderer {
    private Map<Integer, String> listaFiltros = new HashMap<Integer, String>();

    public BoxSessionCellRendered(Map<Integer, String> listaFiltros) {
        this.listaFiltros = listaFiltros;
    }

    public static void setCellRenderer(JTable table,Map<Integer, String> listaFiltros){
        BoxSessionCellRendered cellRendered=new BoxSessionCellRendered(listaFiltros);
        for (int i=0;i<table.getColumnCount();i++){
            table.getColumnModel().getColumn(i).setCellRenderer(cellRendered);
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component=super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(table.getColumnClass(column).equals(JButton.class)){
//            table.getColumnModel().getColumn(column).setMaxWidth(85);
//            table.getColumnModel().getColumn(column).setMinWidth(85);
            table.getColumnModel().getColumn(column).setPreferredWidth(85);
            JButton button=(JButton) value;
            button.setBackground(component.getBackground());
            button.setForeground(component.getForeground());
            return button;
        }else{
            DesingTxtTable componente=buscarTexto2(listaFiltros,value,column,component);
            switch(table.getColumnName(column)){
                case "NRO.":
                    componente.setHorizontalAlignment(SwingConstants.CENTER);
//                    table.getColumn(table.getColumnName(column)).setMaxWidth(90);
//                    table.getColumn(table.getColumnName(column)).setMinWidth(90);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(90);
                    break;
                case "EFECTIVO":
                case "TRANSFERENCIA":
                case "TOTAL":
                    componente.setHorizontalAlignment(SwingConstants.RIGHT);
//                    table.getColumn(table.getColumnName(column)).setMaxWidth(95);
//                    table.getColumn(table.getColumnName(column)).setMinWidth(95);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(95);
                    break;
                case "INICIO":
                case "CIERRE":
                    componente.setHorizontalAlignment(SwingConstants.CENTER);
//                    table.getColumn(table.getColumnName(column)).setMaxWidth(150);
//                    table.getColumn(table.getColumnName(column)).setMinWidth(150);
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
