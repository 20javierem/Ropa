package com.babas.utilitiesTables.tablesCellRendered;

import com.babas.views.dialogs.DesingTxtTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.babas.utilitiesTables.UtilitiesTables.buscarTexto2;

public class RentalCellRendered extends DefaultTableCellRenderer {
    private Map<Integer, String> listaFiltros = new HashMap<Integer, String>();

    public RentalCellRendered(Map<Integer, String> listaFiltros) {
        this.listaFiltros = listaFiltros;
    }

    public static void setCellRenderer(JTable table,Map<Integer, String> listaFiltros){
        RentalCellRendered cellRendered=new RentalCellRendered(listaFiltros);
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
            DesingTxtTable componente=buscarTexto2(listaFiltros,value,column,component);
            switch(table.getColumnName(column)){
                case "NRO.":
                    componente.setHorizontalAlignment(SwingConstants.CENTER);
                    table.getColumn(table.getColumnName(column)).setMaxWidth(110);
                    table.getColumn(table.getColumnName(column)).setMinWidth(110);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(110);
                    break;
                case "RESUMEN":
                case "SUBTOTAL":
                case "MULTA":
                case "TOTAL":
                case "DESCUENTO":
                case "TOTAL-ACTUAL":
                case "MONTO":
                case "GARANT??A":
                    componente.setHorizontalAlignment(SwingConstants.RIGHT);
//                    table.getColumn(table.getColumnName(column)).setMaxWidth(95);
//                    table.getColumn(table.getColumnName(column)).setMinWidth(95);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(95);
                    break;
                case "SUNAT":
                case "FECHA":
                case "ACTUALIZADO":
                case "ESTADO":
                case "TIPO":
                    componente.setHorizontalAlignment(SwingConstants.CENTER);
//                    table.getColumn(table.getColumnName(column)).setMaxWidth(150);
//                    table.getColumn(table.getColumnName(column)).setMinWidth(150);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(120);
                    break;
                case "TIPO/PAGO":
                    componente.setHorizontalAlignment(SwingConstants.LEFT);
//                    table.getColumn(table.getColumnName(column)).setMaxWidth(120);
//                    table.getColumn(table.getColumnName(column)).setMinWidth(120);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(120);
                    break;
                default:
                    componente.setHorizontalAlignment(SwingConstants.LEFT);
                    break;
            }
            return componente.getContentPane();
        }
    }

}
