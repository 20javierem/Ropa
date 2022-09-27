package com.babas.utilitiesTables.buttonEditors;

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

public class JButtonEditorDetailSale extends DefaultCellEditor {

    public JButtonEditorDetailSale() {
        super(new JComboBox());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JComboBox comboBox=new JComboBox();
        comboBox.setBorder(null);
        DetailSale detailSale= (DetailSale) value;
        comboBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                stopCellEditing();
            }
        });
        if(column == 5){
            comboBox.setEditable(false);
            comboBox.setModel(new DefaultComboBoxModel(new Vector(detailSale.getProduct().getPresentations())));
            comboBox.setRenderer(new Presentation.ListCellRenderer());
            comboBox.setSelectedItem(detailSale.getPresentation());
            comboBox.addActionListener(e -> {
                Presentation presentation=(Presentation) comboBox.getSelectedItem();
                detailSale.setPresentation(presentation);
                detailSale.setPrice(presentation.getPriceDefault().getPrice());
                stopCellEditing();
                Utilities.getTabbedPane().updateTab();
            });
        }else{
            comboBox.setEditable(true);
            detailSale.getPresentation().getPrices().forEach(price -> {
                comboBox.addItem(price.getPrice());
            });
            comboBox.setSelectedItem(detailSale.getPrice());
            comboBox.addActionListener(e -> {
                detailSale.setPrice(Double.valueOf(((JTextField)comboBox.getEditor().getEditorComponent()).getText()));
                stopCellEditing();
                Utilities.getTabbedPane().updateTab();
            });
            comboBox.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode()==KeyEvent.VK_ENTER){
                        detailSale.setPrice(Double.valueOf(((JTextField)comboBox.getEditor().getEditorComponent()).getText()));
                        stopCellEditing();
                        Utilities.getTabbedPane().updateTab();
                    }
                }
            });
        }
        return comboBox;
    }
}
