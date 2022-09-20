package com.babas.utilitiesTables.buttonEditors;

import com.babas.models.Category;
import com.babas.models.Transfer;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.CategoryAbstractModel;
import com.babas.utilitiesTables.tablesModels.TransferAbstractModel;
import com.babas.views.dialogs.DCategory;
import com.babas.views.frames.FPrincipal;
import com.babas.views.tabs.TabNewTraslade;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonEditorTransfer extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;
    private boolean edit;

    public JButtonEditorTransfer() {
        button=new JButtonAction("x16/mostrarContraseña.png");
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
            Transfer transfer=((TransferAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            TabNewTraslade tabNewTraslade=new TabNewTraslade(transfer);
            if(Utilities.getTabbedPane().indexOfTab(tabNewTraslade.getTabPane().getTitle())==-1){
                Utilities.getTabbedPane().addTab(tabNewTraslade.getTabPane().getTitle(),tabNewTraslade.getTabPane());
            }
            Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(tabNewTraslade.getTabPane().getTitle()));
            Utilities.updateDialog();
            fireEditingStopped();
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
