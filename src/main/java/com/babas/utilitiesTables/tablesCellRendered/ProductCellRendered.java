package com.babas.utilitiesTables.tablesCellRendered;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Map;

import static com.babas.utilitiesTables.UtilitiesTables.buscarTexto;
import static com.babas.utilitiesTables.UtilitiesTables.buscarTexto2;

public class ProductCellRendered extends DefaultTableCellRenderer {
    private Map<Integer, String> listaFiltros;

    public ProductCellRendered(Map<Integer, String> listaFiltros){
        this.listaFiltros=listaFiltros;
    }

    public static void setCellRenderer(JTable table,Map<Integer,String> listaFiltros){
        ProductCellRendered cellRendered=new ProductCellRendered(listaFiltros);
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
            JPanel componente=buscarTexto2(listaFiltros,value,column,component);
            switch(table.getColumnName(column)){
                case "GÉNERO":
//                    componente.setHorizontalAlignment(SwingConstants.CENTER);
                    table.getColumn(table.getColumnName(column)).setMaxWidth(110);
                    table.getColumn(table.getColumnName(column)).setMinWidth(110);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(110);
                    break;
                case "TOTAL-STOCK":
//                    componente.setHorizontalAlignment(SwingConstants.CENTER);
                    table.getColumn(table.getColumnName(column)).setMaxWidth(120);
                    table.getColumn(table.getColumnName(column)).setMinWidth(120);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(120);
                    break;
                case "COLOR":
//                    componente.setHorizontalAlignment(SwingConstants.CENTER);
                    table.getColumn(table.getColumnName(column)).setMaxWidth(75);
                    table.getColumn(table.getColumnName(column)).setMinWidth(75);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(75);
                    break;
                case "TALLA":
//                    componente.setHorizontalAlignment(SwingConstants.CENTER);
                    table.getColumn(table.getColumnName(column)).setMaxWidth(60);
                    table.getColumn(table.getColumnName(column)).setMinWidth(60);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(60);
                    break;
                case "PRECIO":
//                    componente.setHorizontalAlignment(SwingConstants.RIGHT);
                    table.getColumn(table.getColumnName(column)).setMaxWidth(120);
                    table.getColumn(table.getColumnName(column)).setMinWidth(120);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(120);
                    break;
                case "CÓDIGO":
//                    componente.setHorizontalAlignment(SwingConstants.CENTER);
                    table.getColumn(table.getColumnName(column)).setMaxWidth(70);
                    table.getColumn(table.getColumnName(column)).setMinWidth(70);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(70);
                    break;
                default:
//                    componente.setHorizontalAlignment(SwingConstants.LEFT);
                    break;
            }
            return componente;
        }
    }

}
