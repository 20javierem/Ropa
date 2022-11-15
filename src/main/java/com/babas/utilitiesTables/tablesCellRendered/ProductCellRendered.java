package com.babas.utilitiesTables.tablesCellRendered;

import com.babas.views.dialogs.DesingTxtTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Map;

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
            button.setForeground(component.getForeground());
            return button;
        }else{
            DesingTxtTable desingTxtTable=buscarTexto2(listaFiltros,value,column,component);
            switch(table.getColumnName(column)){
                case "GÉNERO":
                    desingTxtTable.setHorizontalAlignment(SwingConstants.LEFT);
//                    table.getColumn(table.getColumnName(column)).setMaxWidth(110);
//                    table.getColumn(table.getColumnName(column)).setMinWidth(110);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(110);
                    break;
                case "MARCA":
                    desingTxtTable.setHorizontalAlignment(SwingConstants.LEFT);
//                    table.getColumn(table.getColumnName(column)).setMaxWidth(150);
//                    table.getColumn(table.getColumnName(column)).setMinWidth(150);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(150);
                    break;
                case "PRECIO":
                    desingTxtTable.setHorizontalAlignment(SwingConstants.RIGHT);
//                    table.getColumn(table.getColumnName(column)).setMaxWidth(110);
//                    table.getColumn(table.getColumnName(column)).setMinWidth(110);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(110);
                    break;
                case "TOTAL-STOCK":
                    desingTxtTable.setHorizontalAlignment(SwingConstants.CENTER);
//                    table.getColumn(table.getColumnName(column)).setMaxWidth(120);
//                    table.getColumn(table.getColumnName(column)).setMinWidth(120);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(90);
                    break;
                case "CATEGORÍA":
                    desingTxtTable.setHorizontalAlignment(SwingConstants.LEFT);
//                    table.getColumn(table.getColumnName(column)).setMaxWidth(120);
//                    table.getColumn(table.getColumnName(column)).setMinWidth(120);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(120);
                    break;
                case "COLOR":
                    desingTxtTable.setHorizontalAlignment(SwingConstants.LEFT);
//                    table.getColumn(table.getColumnName(column)).setMaxWidth(75);
//                    table.getColumn(table.getColumnName(column)).setMinWidth(75);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(75);
                    break;
                case "TALLA":
                    desingTxtTable.setHorizontalAlignment(SwingConstants.LEFT);
//                    table.getColumn(table.getColumnName(column)).setMaxWidth(60);
//                    table.getColumn(table.getColumnName(column)).setMinWidth(60);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(60);
                    break;
                case "CÓDIGO":
                    desingTxtTable.setHorizontalAlignment(SwingConstants.CENTER);
                    table.getColumn(table.getColumnName(column)).setMaxWidth(100);
                    table.getColumn(table.getColumnName(column)).setMinWidth(100);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(100);
                    break;
                default:
                    desingTxtTable.setHorizontalAlignment(SwingConstants.LEFT);
                    break;
            }
            return desingTxtTable.getContentPane();
        }
    }

}
