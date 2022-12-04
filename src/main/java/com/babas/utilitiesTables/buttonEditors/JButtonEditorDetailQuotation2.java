package com.babas.utilitiesTables.buttonEditors;

import com.babas.App;
import com.babas.models.DetailQuotation;
import com.babas.models.DetailSale;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.DetailQuotationAbstractModel;
import com.babas.utilitiesTables.tablesModels.DetailSaleAbstractModel;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonEditorDetailQuotation2 extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;

    public JButtonEditorDetailQuotation2() {
        button=new JButtonAction(new FlatSVGIcon(App.class.getResource("icons/svg/error.svg")));
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
            boolean si=JOptionPane.showConfirmDialog(Utilities.getJFrame(),"¿Está seguro?","Quitar producto",JOptionPane.YES_NO_OPTION)==0;
            if(si){
                DetailQuotation detailQuotation=((DetailQuotationAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
                detailQuotation.getQuotation().getDetailQuotations().remove(detailQuotation);
                Utilities.getTabbedPane().updateTab();
            }
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
