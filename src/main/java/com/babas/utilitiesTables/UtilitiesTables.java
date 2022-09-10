package com.babas.utilitiesTables;

import com.babas.utilities.Utilities;
import com.formdev.flatlaf.extras.components.FlatTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.Date;
import java.util.Map;

public class UtilitiesTables {

    public static void actualizarTabla(JTable tabla){
        SwingUtilities.invokeLater(tabla::updateUI);
    }

    public static void headerNegrita(FlatTable table){
        table.getTableHeader().setBackground(Color.black);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
    }

    public static JTextField buscarTexto(Map<Integer, String> listaFiltros, Object value, int column, DefaultTableCellRenderer defaultTableCellRenderer) {
        JTextField componente=new JTextField();
        componente.setBorder(defaultTableCellRenderer.getBorder());
        componente.setBackground(defaultTableCellRenderer.getBackground());
        componente.setForeground(defaultTableCellRenderer.getForeground());
        if(value instanceof Date){
            componente.setText(Utilities.formatoFecha.format(value));
        }else{
            if(value instanceof Double){
                componente.setText(Utilities.moneda.format(value));
            }else{
                componente.setText(String.valueOf(value));
            }
        }
        if(listaFiltros!=null){
            Highlighter hilit = new DefaultHighlighter();
            Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(componente.getSelectionColor());
            componente.setHighlighter(hilit);
            if(listaFiltros.get(column)!=null){
                String s = listaFiltros.get(column).toLowerCase();
                if (s.length() > 0) {
                    String contenido = componente.getText().toLowerCase();
                    int index = contenido.indexOf(s);
                    if (index >= 0) {
                        try {
                            int end = index + s.length();
                            hilit.addHighlight(index, end, painter);
                            componente.setCaretPosition(end);
                        } catch (BadLocationException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return componente;
    }

    public static Component isSelected(Component component,boolean isSelected, JTable table){
        if(isSelected){
            component.setBackground(table.getSelectionBackground());
            component.setForeground(table.getSelectionForeground());
        }else{
            component.setBackground(table.getBackground());
            component.setForeground(table.getForeground());
        }
        return component;
    }

    public static void pintarComponente(Component component,boolean estado,boolean isSelected){
        if(!estado){
            if(!isSelected){
                component.setBackground(new Color(0xFFFF0000, true));
                component.setForeground(new Color(0xFFFFFF));
            }
        }
    }

}
