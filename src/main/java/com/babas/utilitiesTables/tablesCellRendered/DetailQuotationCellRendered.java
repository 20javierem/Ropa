package com.babas.utilitiesTables.tablesCellRendered;

import com.babas.models.DetailQuotation;
import com.babas.models.DetailSale;
import com.babas.models.Price;
import com.babas.views.dialogs.DesingTxtTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import static com.babas.utilitiesTables.UtilitiesTables.buscarTexto2;

public class DetailQuotationCellRendered extends DefaultTableCellRenderer {

    public DetailQuotationCellRendered(){

    }

    public static void setCellRenderer(JTable table){
        DetailQuotationCellRendered cellRendered=new DetailQuotationCellRendered();
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
            DetailQuotation detailQuotation;
            switch(table.getColumnName(column)){
                case "COLOR":
                case "GÉNERO":
                case "TALLA":
                case "T-STOCK":
                case "CANTIDAD":
                    componente.setHorizontalAlignment(SwingConstants.CENTER);
//                    table.getColumn(table.getColumnName(column)).setMaxWidth(120);
//                    table.getColumn(table.getColumnName(column)).setMinWidth(120);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(120);
                    break;
                case "SUBTOTAL":
                    componente.setHorizontalAlignment(SwingConstants.RIGHT);
//                    table.getColumn(table.getColumnName(column)).setMaxWidth(120);
//                    table.getColumn(table.getColumnName(column)).setMinWidth(120);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(120);
                    break;
                case "CÓDIGO":
                    componente.setHorizontalAlignment(SwingConstants.CENTER);
                    table.getColumn(table.getColumnName(column)).setMaxWidth(80);
                    table.getColumn(table.getColumnName(column)).setMinWidth(80);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(80);
                    break;
                case "PRESENTACIÓN":
                    detailQuotation=(DetailQuotation) value;
                    JComboBox comboBox=new JComboBox();
                    comboBox.setBorder(null);
                    comboBox.setForeground(component.getForeground());
                    comboBox.setBackground(component.getBackground());
                    comboBox.addItem(detailQuotation.getNamePresentation());
//                    table.getColumn(table.getColumnName(column)).setMaxWidth(120);
//                    table.getColumn(table.getColumnName(column)).setMinWidth(120);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(120);
                    return comboBox;
                case "PRECIO":
                    detailQuotation = (DetailQuotation) value;
                    comboBox = new JComboBox();
                    comboBox.setBorder(null);
                    comboBox.setForeground(component.getForeground());
                    comboBox.setBackground(component.getBackground());
                    comboBox.setRenderer(new Price.ListCellRenderer());
                    comboBox.addItem(detailQuotation.getPrice());
//                    table.getColumn(table.getColumnName(column)).setMaxWidth(120);
//                    table.getColumn(table.getColumnName(column)).setMinWidth(120);
                    table.getColumn(table.getColumnName(column)).setPreferredWidth(120);
                    return comboBox;
                default:
                    componente.setHorizontalAlignment(SwingConstants.LEFT);
                    break;
            }
            return componente.getContentPane();
        }
    }

}
