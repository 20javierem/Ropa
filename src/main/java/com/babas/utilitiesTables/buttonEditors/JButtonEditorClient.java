package com.babas.utilitiesTables.buttonEditors;

import com.babas.App;
import com.babas.models.Client;
import com.babas.models.User;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilitiesTables.tablesModels.ClientAbstractModel;
import com.babas.utilitiesTables.tablesModels.UserAbstractModel;
import com.babas.views.dialogs.DClient;
import com.babas.views.dialogs.DUser;
import com.babas.views.frames.FPrincipal;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonEditorClient extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;

    public JButtonEditorClient() {
        button=new JButtonAction("edit");
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
            Client client=((ClientAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            DClient dClient=new DClient(false,client);
            dClient.setVisible(true);
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
