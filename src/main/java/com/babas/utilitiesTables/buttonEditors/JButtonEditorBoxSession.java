package com.babas.utilitiesTables.buttonEditors;

import com.babas.models.BoxSession;
import com.babas.models.Movement;
import com.babas.models.Rental;
import com.babas.models.Sale;
import com.babas.utilities.Babas;
import com.babas.utilities.Utilities;
import com.babas.utilities.UtilitiesReports;
import com.babas.utilitiesTables.tablesModels.BoxSesionAbstractModel;
import com.babas.utilitiesTables.tablesModels.SaleAbstractModel;
import com.babas.views.tabs.TabBoxSesion;
import com.babas.views.tabs.TabNewRental;
import com.moreno.Notify;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonEditorBoxSession extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButtonAction button;

    public JButtonEditorBoxSession() {
        button=new JButtonAction("x16/mostrarContraseña.png","Detalle");
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
            BoxSession boxSession=((BoxSesionAbstractModel) table.getModel()).getList().get(table.convertRowIndexToModel(table.getSelectedRow()));
            TabBoxSesion tabBoxSesion=new TabBoxSesion(boxSession);
            if(Utilities.getTabbedPane().indexOfTab(tabBoxSesion.getTabPane().getTitle())==-1){
                Utilities.getTabbedPane().addTab(tabBoxSesion.getTabPane().getTitle(),tabBoxSesion.getTabPane());
            }
            Utilities.getTabbedPane().setSelectedIndex(Utilities.getTabbedPane().indexOfTab(tabBoxSesion.getTabPane().getTitle()));
            fireEditingStopped();
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