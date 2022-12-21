package com.babas.utilitiesTables.buttonEditors;

import com.babas.App;
import com.babas.models.Brand;
import com.babas.models.DetailTransfer;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.BrandAbstractModel;
import com.babas.utilitiesTables.tablesModels.DetailTransferAbstractModel;
import com.babas.views.dialogs.DBrand;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonEditorDetailTransfer extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;

    public JButtonEditorDetailTransfer() {
        button=new JButtonAction("error");
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
                DetailTransfer detailTransfer=((DetailTransferAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
                detailTransfer.getTransfer().getDetailTransfers().remove(detailTransfer);
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
