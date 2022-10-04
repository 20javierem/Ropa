package com.babas.utilitiesTables.buttonEditors;

import com.babas.models.DetailRental;
import com.babas.models.DetailSale;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.DetailRentalAbstractModel;
import com.babas.utilitiesTables.tablesModels.DetailSaleAbstractModel;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonEditorDetailRental2 extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;

    public JButtonEditorDetailRental2() {
        button=new JButtonAction("x16/remove.png");
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        button.setActionCommand("edit");
        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTable table = (JTable)button.getParent();
        if(table.getSelectedRow()!=-1){
            fireEditingStopped();
            DetailRental detailRental=((DetailRentalAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            detailRental.getRental().getDetailRentals().remove(detailRental);
            Utilities.getTabbedPane().updateTab();
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button;
    }
}
