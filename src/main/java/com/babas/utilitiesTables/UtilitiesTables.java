package com.babas.utilitiesTables;

import com.babas.utilities.Utilities;
import com.babas.views.dialogs.DesingTxtTable;
import com.formdev.flatlaf.extras.components.FlatTable;
import org.apache.commons.math3.analysis.function.Abs;

import javax.swing.*;
import javax.swing.plaf.LabelUI;
import javax.swing.plaf.TextUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.util.Date;
import java.util.Map;

public class UtilitiesTables {

    public static void headerNegrita(FlatTable table){
        table.getTableHeader().setBorder(null);
        table.getTableHeader().setBackground(Color.black);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
    }

    public static JTextField buscarTexto(Map<Integer, String> listaFiltros, Object value, int column, Component component) {
        JTextField componente=new JTextField();
        componente.setBorder(null);
        componente.setBackground(component.getBackground());
        componente.setForeground(component.getForeground());
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
                            componente.setSelectionEnd(end);
                        } catch (BadLocationException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return componente;
    }

    public static JPanel buscarTexto2(Map<Integer, String> listaFiltros, Object value, int column, Component component) {
        DesingTxtTable desingTxtTable=new DesingTxtTable(component);
        if(value instanceof Date){
            desingTxtTable.setString0(Utilities.formatoFecha.format(value));
            desingTxtTable.setString1(null);
            desingTxtTable.setString2(null);
        }else{
            if(value instanceof Double){
                desingTxtTable.setString0(Utilities.moneda.format(value));
                desingTxtTable.setString1(null);
                desingTxtTable.setString2(null);
            }else{
                desingTxtTable.setString0(String.valueOf(value));
                desingTxtTable.setString1(null);
                desingTxtTable.setString2(null);
            }
        }
        if(listaFiltros!=null){
            if(listaFiltros.get(column)!=null){
                String s = listaFiltros.get(column).toLowerCase();
                if (s.length() > 0) {
                    String contenido = String.valueOf(value);
                    desingTxtTable.setString0(contenido);
                    desingTxtTable.setString1(null);
                    desingTxtTable.setString2(null);
                    System.out.println(contenido);
                    int index = contenido.toLowerCase().indexOf(s);
                    if (index >= 0) {
                        int end = index + s.length();
                        desingTxtTable.setString0(contenido.substring(0,index));
                        desingTxtTable.setString1(contenido.substring(index,end));
                        desingTxtTable.setString2(contenido.substring(end));
                    }
                }
            }
        }
        return desingTxtTable.getContentPane();
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
