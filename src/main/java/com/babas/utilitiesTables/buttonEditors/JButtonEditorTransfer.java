package com.babas.utilitiesTables.buttonEditors;

import com.babas.App;
import com.babas.controllers.Stocks;
import com.babas.models.Category;
import com.babas.models.Stock;
import com.babas.models.Transfer;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.CategoryAbstractModel;
import com.babas.utilitiesTables.tablesModels.TransferAbstractModel;
import com.babas.views.dialogs.DCategory;
import com.babas.views.frames.FPrincipal;
import com.babas.views.tabs.TabNewTraslade;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class JButtonEditorTransfer extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;
    private boolean show;

    public JButtonEditorTransfer(boolean show) {
        this.show=show;
        if(show){
            button=new JButtonAction("show");
        }else{
            button=new JButtonAction("error");
        }

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
            Transfer transfer=((TransferAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            if(show){
                transfer.showTicket();
            }else{
                transfer.cancelTransfer();
            }
            Utilities.getTabbedPane().updateTab();
            Utilities.updateDialog();
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
