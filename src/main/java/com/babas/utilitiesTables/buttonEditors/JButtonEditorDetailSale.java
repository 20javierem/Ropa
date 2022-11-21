package com.babas.utilitiesTables.buttonEditors;

import com.babas.models.DetailSale;
import com.babas.models.Presentation;
import com.babas.utilities.Utilities;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

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
            comboBox.setEditable(true);
            comboBox.setModel(new DefaultComboBoxModel(new Vector(detailSale.getProduct().getPresentations())));
            comboBox.setSelectedItem(detailSale.getNamePresentation());
            comboBox.addActionListener(e -> {
                if(comboBox.getSelectedItem() instanceof Presentation){
                    Presentation presentation=(Presentation) comboBox.getSelectedItem();
                    detailSale.setPresentation(presentation);
                    detailSale.setPrice(presentation.getPriceDefault().getPrice());
                }else{
                    detailSale.setPresentation(null);
                    detailSale.setNamePresentation(comboBox.getSelectedItem().toString());
                }
                stopCellEditing();
                Utilities.getTabbedPane().updateTab();
            });
        }else{
            comboBox.setEditable(true);
            if(detailSale.getPresentation()!=null){
                detailSale.getPresentation().getPrices().forEach(price -> {
                    comboBox.addItem(price.getPrice());
                });
            }else{
                comboBox.addItem(detailSale.getPrice());
            }
            comboBox.setSelectedItem(detailSale.getPrice());
            comboBox.addActionListener(e -> {
                try{
                    detailSale.setPrice(Double.valueOf(((JTextField)comboBox.getEditor().getEditorComponent()).getText()));
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
                            detailSale.setPrice(Double.valueOf(((JTextField)comboBox.getEditor().getEditorComponent()).getText()));
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
