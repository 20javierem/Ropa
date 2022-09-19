package com.babas.utilitiesTables.tablesCellRendered;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Map;

import static com.babas.utilitiesTables.UtilitiesTables.buscarTexto;

public class StockCellRendered extends DefaultTableCellRenderer {

    private Map<Integer, String> listaFiltros;

    public StockCellRendered(Map<Integer, String> listaFiltros){
        this.listaFiltros=listaFiltros;
    }

    public static void setCellRenderer(JTable table,Map<Integer,String> listaFiltros){
        StockCellRendered cellRendered=new StockCellRendered(listaFiltros);
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
            JTextField componente=buscarTexto(listaFiltros,value,column,component);
            switch(table.getColumnName(column)){
                case "PRODUCTO":
                    componente.setHorizontalAlignment(SwingConstants.LEFT);
                    table.getColumn(table.getColumnName(column)).setMaxWidth(200);
                    table.getColumn(table.getColumnName(column)).setMinWidth(200);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(200);
                    break;
                case "STOCK":
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
