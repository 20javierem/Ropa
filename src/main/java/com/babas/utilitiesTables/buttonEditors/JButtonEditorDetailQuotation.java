package com.babas.utilitiesTables.buttonEditors;

import com.babas.models.DetailQuotation;
import com.babas.models.DetailSale;
import com.babas.models.Presentation;
import com.babas.utilities.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

public class JButtonEditorDetailQuotation extends DefaultCellEditor {

    public JButtonEditorDetailQuotation() {
        super(new JComboBox());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JComboBox comboBox=new JComboBox();
        comboBox.setBorder(null);
        DetailQuotation detailQuotation= (DetailQuotation) value;
        comboBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                stopCellEditing();
            }
        });
        if(column == 5){
            comboBox.setEditable(false);
            comboBox.setModel(new DefaultComboBoxModel(new Vector(detailQuotation.getProduct().getPresentations())));
            comboBox.setRenderer(new Presentation.ListCellRenderer());
            comboBox.setSelectedItem(detailQuotation.getPresentation());
            comboBox.addActionListener(e -> {
                Presentation presentation=(Presentation) comboBox.getSelectedItem();
                detailQuotation.setPresentation(presentation);
                detailQuotation.setPrice(presentation.getPriceDefault().getPrice());
                stopCellEditing();
                Utilities.getTabbedPane().updateTab();
            });
        }else{
            comboBox.setEditable(true);
            detailQuotation.getPresentation().getPrices().forEach(price -> {
                comboBox.addItem(price.getPrice());
            });
            comboBox.setSelectedItem(detailQuotation.getPrice());
            comboBox.addActionListener(e -> {
                try{
                    detailQuotation.setPrice(Double.valueOf(((JTextField)comboBox.getEditor().getEditorComponent()).getText()));
                }catch (NumberFormatException ignored){

                }
                stopCellEditing();
                Utilities.getTabbedPane().updateTab();
            });
            comboBox.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode()==KeyEvent.VK_ENTER){
                        try{
                            detailQuotation.setPrice(Double.valueOf(((JTextField)comboBox.getEditor().getEditorComponent()).getText()));
                        }catch (NumberFormatException ignored){

                        }
                        stopCellEditing();
                        Utilities.getTabbedPane().updateTab();
                    }
                }
            });
        }
        return comboBox;
    }
}
