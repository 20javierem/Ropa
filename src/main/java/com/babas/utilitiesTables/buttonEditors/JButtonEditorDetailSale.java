package com.babas.utilitiesTables.buttonEditors;

import com.babas.models.DetailSale;
import com.babas.models.Presentation;
import com.babas.models.Price;
import com.babas.models.Sale;
import com.babas.utilities.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

public class JButtonEditorDetailSale extends DefaultCellEditor {
    private Sale sale;
    private boolean presentation;

    public JButtonEditorDetailSale(Sale sale,boolean presentation) {
        super(new JComboBox());
        this.sale=sale;
        this.presentation=presentation;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JComboBox comboBox=new JComboBox();
        DetailSale detailSale=sale.getDetailSales().get(row);
        if(presentation){
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
